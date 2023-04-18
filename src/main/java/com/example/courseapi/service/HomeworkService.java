package com.example.courseapi.service;

import com.example.courseapi.dto.HomeworkDTO;
import com.example.courseapi.domain.Homework;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Homework}.
 */
public interface HomeworkService {

    HomeworkDTO save(HomeworkDTO homeworkDTO);

    List<HomeworkDTO> findAll();

    Optional<HomeworkDTO> findById(Long homeworkId);

    void delete(Long homeworkId);

    List<HomeworkDTO> findByStudent(Long studentId);

    List<HomeworkDTO> findByLessonId(Long lessonId);
}
