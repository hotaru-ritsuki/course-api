package com.example.courseapi.service;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.LessonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Lesson} entity.
 */
public interface LessonService {

    /**
     * Finds a {@link LessonDTO} by ID.
     *
     * @param id the ID of the {@link LessonDTO} to find.
     * @return an {@link Optional} with the {@link LessonDTO}, or empty if not found.
     */
    Optional<LessonDTO> findById(Long id);

    /**
     * Saves a {@link LessonDTO}.
     *
     * @param lessonDTO the {@link LessonDTO} to save.
     * @return the saved {@link LessonDTO}.
     */
    LessonDTO save(LessonDTO lessonDTO);

    /**
     * Finds all {@link LessonDTO} objects that match the given filters and pageable.
     *
     * @param filters  the optional filters to apply when searching for courses
     * @param pageable the pagination information to apply when retrieving the courses
     * @return a {@link Page} with the {@link LessonDTO} objects found.
     */
    Page<LessonDTO> findAll(Filters filters, Pageable pageable);

    /**
     * Finds all {@link LessonDTO} objects associated with a specific course.
     *
     * @param courseId the ID of the course.
     * @return a list with the {@link LessonDTO} objects associated with the course.
     */
    List<LessonDTO> findByCourseId(Long courseId);

    /**
     * Deletes a {@link LessonDTO} by ID.
     *
     * @param lessonId the ID of the {@link LessonDTO} to delete.
     */
    void delete(Long lessonId);
}
