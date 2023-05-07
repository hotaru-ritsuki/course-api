package com.example.courseapi.service;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.domain.Student;
import com.example.courseapi.dto.request.CourseFeedbackRequestDTO;
import com.example.courseapi.dto.response.CourseFeedbackResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * An interface for managing {@link CourseFeedback} entity.
 */
public interface CourseFeedbackService {

    /**
     * Finds a course feedback entry by its ID.
     *
     * @param id the ID of the course feedback entry to find
     * @return an optional containing the found course feedback DTO, or empty if not found
     */
    Optional<CourseFeedbackResponseDTO> findById(final Long id);

    /**
     * Saves a new course feedback entry or updates an existing one.
     *
     * @param courseDTO the course feedback DTO to save or update
     * @return the saved or updated course feedback DTO
     */
    CourseFeedbackResponseDTO save(final CourseFeedbackRequestDTO courseDTO, final Long studentId);

    /**
     * Finds all course feedback entries with optional filters and pagination.
     *
     * @param filters  the optional filters to apply when searching for course feedback entries
     * @param pageable the pagination information to apply when retrieving the course feedback entries
     * @return a page containing the found course feedback DTOs
     */
    Page<CourseFeedbackResponseDTO> findAll(final Filters filters, final Pageable pageable);

    /**
     * Finds all course feedback entries associated with a specific student ID.
     *
     * @param studentId the ID of the student to find course feedback entries for
     * @return a list containing the found course feedback DTOs
     */
    List<CourseFeedbackResponseDTO> findByStudentId(final Long studentId);

    /**
     * Finds all course feedback entries associated with a specific course ID.
     *
     * @param courseId the ID of the course to find course feedback entries for
     * @return a list containing the found course feedback DTOs
     */
    List<CourseFeedbackResponseDTO> findByCourseId(final Long courseId);

    /**
     * Deletes a course feedback entry with the given ID.
     *
     * @param courseId the ID of the course feedback entry to delete
     */
    void delete(final Long courseId);
}
