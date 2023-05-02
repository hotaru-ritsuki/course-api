package com.example.courseapi.service.impl;

import com.example.courseapi.config.args.generic.FilterImpl;
import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.generic.SpecificationComparison;
import com.example.courseapi.config.args.specs.SpecificationBuilder;
import com.example.courseapi.domain.*;
import com.example.courseapi.dto.request.HomeworkRequestDTO;
import com.example.courseapi.dto.response.HomeworkResponseDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.HomeworkRepository;
import com.example.courseapi.repository.InstructorRepository;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.service.HomeworkService;
import com.example.courseapi.service.mapper.HomeworkMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final LessonRepository lessonRepository;
    private final HomeworkMapper homeworkMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<HomeworkResponseDTO> findById(final Long homeworkId) {
        log.debug("Finding homework with id: {}", homeworkId);
        return homeworkRepository.findById(homeworkId).map(homeworkMapper::toResponseDto);
    }

    @Override
    @Transactional
    public HomeworkResponseDTO save(final HomeworkRequestDTO homeworkDTO) {
        log.debug("Saving homework : {}", homeworkDTO);
        Homework homework = homeworkMapper.fromRequestDto(homeworkDTO);
        homework = homeworkRepository.save(homework);

        return homeworkMapper.toResponseDto(homework);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HomeworkResponseDTO> findAll(final Filters filters, final Pageable pageable, final User user) {
        log.debug("Finding all homeworks by filters and pageable");
        if (user instanceof Student) {
            Student student = studentRepository.findById(user.getId()).orElseThrow(() ->
                    new SystemException("Student with id: " + user.getId() + " not found.", ErrorCode.FORBIDDEN));
            List<Long> lessonIds = student.getStudentCourses().stream()
                    .map(Course::getLessons).flatMap(Set::stream).map(Lesson::getId).toList();
            log.debug("Current user is student. Additional filters will be applied. \n" +
                    " Finding all homeworks with related lessons in {}", lessonIds);
            filters.include(new FilterImpl("lesson", SpecificationComparison.IN, lessonIds));
        } else if (user instanceof Instructor) {
            Instructor instructor = instructorRepository.findById(user.getId()).orElseThrow(() ->
                    new SystemException("Student with id: " + user.getId() + " not found.", ErrorCode.FORBIDDEN));
            List<Long> lessonIds = instructor.getInstructorCourses().stream()
                    .map(Course::getLessons).flatMap(Set::stream).map(Lesson::getId).toList();
            log.debug("Current user is instructor. Additional filters will be applied. \n" +
                    " Finding all homeworks with related lessons in {}", lessonIds);
            filters.include(new FilterImpl("lesson", SpecificationComparison.IN, lessonIds));
        }
        return homeworkRepository.findAll(new SpecificationBuilder<Homework>(filters).build(), pageable)
                .map(homeworkMapper::toResponseDto);
    }

    @Override
    @Transactional
    public void delete(final Long homeworkId) {
        log.debug("Deleting homework by id: {}", homeworkId);
        homeworkRepository.deleteById(homeworkId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeworkResponseDTO> findByStudent(final Long studentId) {
        log.debug("Finding all homeworks by student id: {}", studentId);
        return homeworkMapper.toResponseDto(homeworkRepository.findByStudentId(studentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeworkResponseDTO> findByLessonId(final Long lessonId) {
        log.debug("Finding all homeworks by lesson id: {}", lessonId);
        return homeworkMapper.toResponseDto(homeworkRepository.findByLessonId(lessonId));
    }

    @Override
    @Transactional
    public HomeworkResponseDTO uploadHomeworkForLesson(
            final Long lessonId, final MultipartFile file, final Long studentId) {
        log.debug("Uploading homework for lesson with id: {} and student id: {}", lessonId, studentId);
        Student student = studentRepository.findById(studentId).orElseThrow(() ->
                        new SystemException("Student with id: " + studentId + " not found.", ErrorCode.BAD_REQUEST));
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() ->
                new SystemException("Lesson with id: " + lessonId + " not found.", ErrorCode.BAD_REQUEST));

        if (!student.getStudentCourses().contains(lesson.getCourse())) {
            throw new SystemException("Student is not subscribed to course with id: " + lesson.getCourse().getId(),
                    ErrorCode.FORBIDDEN);
        }

        String filePath = uploadHomeworkFile(student, lesson, file);
        Homework homework = Homework.builder()
                .student(student)
                .lesson(lesson)
                .title(file.getName())
                .filePath(filePath)
                .build();
        homework = homeworkRepository.save(homework);
        return homeworkMapper.toResponseDto(homework);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Homework> findAllByStudentIdAndCourseId(final Long studentId, final Long courseId) {
        log.debug("Finding all homeworks by student id: {} and course id: {}", studentId, courseId);
        return homeworkRepository.findByStudentIdAndLessonId(studentId, courseId);
    }

    @SuppressWarnings("unused")
    private String uploadHomeworkFile(final Student student, final Lesson lesson, final MultipartFile file) {
        // TODO implementation with S3 Client
        return RandomStringUtils.randomAlphabetic(10) + "/" + file.getName();
    }
}
