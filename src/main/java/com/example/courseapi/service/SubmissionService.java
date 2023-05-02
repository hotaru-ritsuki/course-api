package com.example.courseapi.service;

import com.example.courseapi.domain.Submission;
import com.example.courseapi.dto.request.SubmissionRequestDTO;
import com.example.courseapi.dto.response.SubmissionResponseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Submission} entity.
 */
public interface SubmissionService {

    /**
     * Saves a new submission and assigns it to an instructor.
     *
     * @param submissionRequestDTO the submission to save.
     * @return the saved submission DTO.
     */
    SubmissionResponseDTO save(final SubmissionRequestDTO submissionRequestDTO);

    /**
     * Retrieves a list of all submissions.
     *
     * @return a list of all submission DTOs.
     */
    List<SubmissionResponseDTO> findAll();

    /**
     * Retrieves a submission by lesson and student id.
     *
     * @param lessonId  the id of the lesson.
     * @param studentId the id of the student.
     * @return an optional submission DTO.
     */
    Optional<SubmissionResponseDTO> findById(final Long lessonId, final Long studentId);

    /**
     * Deletes a submission by lesson and student id.
     *
     * @param lessonId  the id of the lesson.
     * @param studentId the id of the student.
     */
    void delete(final Long lessonId, final Long studentId);

    /**
     * Retrieves a list of all submissions for a specific student and course.
     *
     * @param studentId the id of the student.
     * @param courseId  the id of the course.
     * @return a list of submission DTOs.
     */
    List<Submission> findAllByStudentIdAndCourseId(final Long studentId, final Long courseId);

    /**
     * Saves the grade for a submission.
     *
     * @param lessonId     the id of the lesson.
     * @param studentId    the id of the student.
     * @param grade        the grade to save.
     * @return the saved submission DTO with the updated grade.
     */
    SubmissionResponseDTO saveGrade(final Long lessonId, final Long studentId, final Double grade);

    /**
     * Retrieves a list of all submissions for a specific lesson, accessible by the current user.
     *
     * @param lessonId      the id of the lesson.
     * @param currentUserId the id of the current user.
     * @return a list of submission DTOs.
     */
    List<SubmissionResponseDTO> findAllByLesson(final Long lessonId, final Long currentUserId);

    /**
     * Retrieves a list of all submissions for a specific student, accessible by the current user.
     *
     * @param studentId     the id of the student.
     * @param currentUserId the id of the current user.
     * @return a list of submission DTOs.
     */
    List<SubmissionResponseDTO> findAllByStudent(final Long studentId, final Long currentUserId);
}
