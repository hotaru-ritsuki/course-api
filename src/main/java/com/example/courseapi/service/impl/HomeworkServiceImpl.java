package com.example.courseapi.service.impl;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.specs.SpecificationBuilder;
import com.example.courseapi.domain.Homework;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.domain.Student;
import com.example.courseapi.dto.HomeworkDTO;
import com.example.courseapi.exception.LessonNotFoundException;
import com.example.courseapi.exception.StudentNotFoundException;
import com.example.courseapi.exception.StudentNotSubscribedToCourse;
import com.example.courseapi.repository.HomeworkRepository;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.service.HomeworkService;
import com.example.courseapi.service.mapper.HomeworkMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final HomeworkMapper homeworkMapper;


    @Override
    public Optional<HomeworkDTO> findById(Long homeworkId) {
        return homeworkRepository.findById(homeworkId).map(homeworkMapper::toDto);
    }

    @Override
    public HomeworkDTO save(HomeworkDTO homeworkDTO) {
        Homework homework = homeworkMapper.toEntity(homeworkDTO);
        homework = homeworkRepository.save(homework);

        return homeworkMapper.toDto(homework);
    }

    @Override
    public Page<HomeworkDTO> findAll(Filters filters, Pageable pageable) {
        return homeworkRepository.findAll(new SpecificationBuilder<Homework>(filters).build(), pageable)
                .map(homeworkMapper::toDto);
    }

    @Override
    public void delete(Long homeworkId) {
        homeworkRepository.deleteById(homeworkId);
    }

    @Override
    public List<HomeworkDTO> findByStudent(Long studentId) {
        return homeworkMapper.toDto(homeworkRepository.findByStudentId(studentId));
    }

    @Override
    public List<HomeworkDTO> findByLessonId(Long lessonId) {
        return homeworkMapper.toDto(homeworkRepository.findByLessonId(lessonId));
    }

    @Override
    public HomeworkDTO uploadHomeworkForLesson(Long lessonId, MultipartFile file, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(StudentNotFoundException::new);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(LessonNotFoundException::new);

        if (!student.getStudentCourses().contains(lesson.getCourse())) {
            throw new StudentNotSubscribedToCourse();
        }

        String filePath = uploadHomeworkFile(student, lesson, file);
        Homework homework = Homework.builder()
                .student(student)
                .lesson(lesson)
                .title(file.getName())
                .filePath(filePath)
                .build();
        homework = homeworkRepository.save(homework);
        return homeworkMapper.toDto(homework);
    }

    @Override
    public List<Homework> findAllByStudentIdAndCourseId(Long studentId, Long courseId) {
        return homeworkRepository.findByStudentIdAndLessonId(studentId, courseId);
    }

    @SuppressWarnings("unused")
    private String uploadHomeworkFile(Student student, Lesson lesson, MultipartFile file) {
        // TODO implementation with S3 Client
        return RandomStringUtils.randomAlphabetic(10) + "/" + file.getName();
    }
}
