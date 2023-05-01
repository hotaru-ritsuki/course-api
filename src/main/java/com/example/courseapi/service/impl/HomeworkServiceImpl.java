package com.example.courseapi.service.impl;

import com.example.courseapi.config.args.generic.FilterImpl;
import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.generic.SpecificationComparison;
import com.example.courseapi.config.args.specs.SpecificationBuilder;
import com.example.courseapi.domain.*;
import com.example.courseapi.domain.enums.Roles;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    public Optional<HomeworkResponseDTO> findById(Long homeworkId) {
        return homeworkRepository.findById(homeworkId).map(homeworkMapper::toResponseDto);
    }

    @Override
    @Transactional
    public HomeworkResponseDTO save(HomeworkRequestDTO homeworkRequestDTO) {
        Homework homework = homeworkMapper.fromRequestDto(homeworkRequestDTO);
        homework = homeworkRepository.save(homework);

        return homeworkMapper.toResponseDto(homework);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HomeworkResponseDTO> findAll(Filters filters, Pageable pageable, User user) {
        if (user instanceof Student) {
            Student student = studentRepository.findById(user.getId()).orElseThrow(() ->
                    new SystemException("Student with id: " + user.getId() + " not found.", ErrorCode.FORBIDDEN));
            List<Long> lessonIds = student.getStudentCourses().stream()
                    .map(Course::getLessons).flatMap(Set::stream).map(Lesson::getId).toList();
            filters.include(new FilterImpl("lessonId", SpecificationComparison.IN, lessonIds));
        } else if (user instanceof Instructor) {
            Instructor instructor = instructorRepository.findById(user.getId()).orElseThrow(() ->
                    new SystemException("Student with id: " + user.getId() + " not found.", ErrorCode.FORBIDDEN));
            List<Long> lessonIds = instructor.getInstructorCourses().stream()
                    .map(Course::getLessons).flatMap(Set::stream).map(Lesson::getId).toList();
            filters.include(new FilterImpl("lessonId", SpecificationComparison.IN, lessonIds));
        }
        return homeworkRepository.findAll(new SpecificationBuilder<Homework>(filters).build(), pageable)
                .map(homeworkMapper::toResponseDto);
    }

    @Override
    @Transactional
    public void delete(Long homeworkId) {
        homeworkRepository.deleteById(homeworkId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeworkResponseDTO> findByStudent(Long studentId) {
        return homeworkMapper.toResponseDto(homeworkRepository.findByStudentId(studentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeworkResponseDTO> findByLessonId(Long lessonId) {
        return homeworkMapper.toResponseDto(homeworkRepository.findByLessonId(lessonId));
    }

    @Override
    @Transactional
    public HomeworkResponseDTO uploadHomeworkForLesson(Long lessonId, MultipartFile file, Long studentId) {
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
    public List<Homework> findAllByStudentIdAndCourseId(Long studentId, Long courseId) {
        return homeworkRepository.findByStudentIdAndLessonId(studentId, courseId);
    }

    @SuppressWarnings("unused")
    private String uploadHomeworkFile(Student student, Lesson lesson, MultipartFile file) {
        // TODO implementation with S3 Client
        return RandomStringUtils.randomAlphabetic(10) + "/" + file.getName();
    }
}
