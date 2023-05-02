package com.example.courseapi.service;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.request.LessonRequestDTO;
import com.example.courseapi.dto.response.LessonResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Lesson} entity.
 */
public interface LessonService {

    /**
     * Finds a {@link LessonResponseDTO} by ID.
     *
     * @param id the ID of the {@link LessonResponseDTO} to find.
     * @return an {@link Optional} with the {@link LessonResponseDTO}, or empty if not found.
     */
    Optional<LessonResponseDTO> findById(final Long id);

    /**
     * Saves a {@link LessonRequestDTO}.
     *
     * @param lessonRequestDTO the {@link LessonRequestDTO} to save.
     * @return the saved {@link LessonResponseDTO}.
     */
    LessonResponseDTO save(final LessonRequestDTO lessonRequestDTO);

    /**
     * Finds all {@link LessonResponseDTO} objects that match the given filters and pageable.
     *
     * @param filters  the optional filters to apply when searching for courses
     * @param pageable the pagination information to apply when retrieving the courses
     * @return a {@link Page} with the {@link LessonResponseDTO} objects found.
     */
    Page<LessonResponseDTO> findAll(final Filters filters, final Pageable pageable);

    /**
     * Finds all {@link LessonResponseDTO} objects associated with a specific course.
     *
     * @param courseId the ID of the course.
     * @return a list with the {@link LessonResponseDTO} objects associated with the course.
     */
    List<LessonResponseDTO> findByCourseId(final Long courseId);

    /**
     * Deletes a {@link LessonResponseDTO} by ID.
     *
     * @param lessonId the ID of the {@link LessonResponseDTO} to delete.
     */
    void delete(final Long lessonId);
}
