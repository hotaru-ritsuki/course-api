package com.example.courseapi.service;

import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.LessonDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Lesson}.
 */
public interface LessonService {

    Optional<LessonDTO> findById(Long id);

    /**
     * Save a course.
     *
     * @param lessonDTO the entity to save.
     * @return the persisted entity.
     */
    LessonDTO save(LessonDTO lessonDTO);

    List<LessonDTO> findAll();
    List<LessonDTO> findByCourseId(Long courseId);

    void delete(Long lessonId);

}
