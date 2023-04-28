package com.example.courseapi.service;

import com.example.courseapi.domain.Submission;
import com.example.courseapi.domain.User;
import com.example.courseapi.dto.GradeDTO;
import com.example.courseapi.dto.SubmissionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Submission} entity.
 */
public interface SubmissionService {

    /**
     * Saves a new submission and assigns it to an instructor.
     *
     * @param submissionDTO the submission to save.
     * @param instructorId  the id of the instructor to assign the submission to.
     * @return the saved submission DTO.
     */
    SubmissionDTO save(SubmissionDTO submissionDTO, Long instructorId);

    /**
     * Retrieves a list of all submissions.
     *
     * @return a list of all submission DTOs.
     */
    List<SubmissionDTO> findAll();

    /**
     * Retrieves a submission by lesson and student id.
     *
     * @param lessonId  the id of the lesson.
     * @param studentId the id of the student.
     * @return an optional submission DTO.
     */
    Optional<SubmissionDTO> findById(Long lessonId, Long studentId);

    /**
     * Deletes a submission by lesson and student id.
     *
     * @param lessonId  the id of the lesson.
     * @param studentId the id of the student.
     */
    void delete(Long lessonId, Long studentId);

    /**
     * Retrieves a list of all submissions for a specific student and course.
     *
     * @param studentId the id of the student.
     * @param courseId  the id of the course.
     * @return a list of submission DTOs.
     */
    List<Submission> findAllByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * Saves the grade for a submission.
     *
     * @param lessonId     the id of the lesson.
     * @param studentId    the id of the student.
     * @param grade        the grade to save.
     * @param instructorId the id of the instructor.
     * @return the saved submission DTO with the updated grade.
     */
    SubmissionDTO saveGrade(Long lessonId, Long studentId, Double grade, Long instructorId);

    /**
     * Retrieves a list of all submissions for a specific lesson, accessible by the current user.
     *
     * @param lessonId      the id of the lesson.
     * @param currentUserId the id of the current user.
     * @return a list of submission DTOs.
     */
    List<SubmissionDTO> findAllByLesson(Long lessonId, Long currentUserId);

    /**
     * Retrieves a list of all submissions for a specific student, accessible by the current user.
     *
     * @param studentId     the id of the student.
     * @param currentUserId the id of the current user.
     * @return a list of submission DTOs.
     */
    List<SubmissionDTO> findAllByStudent(Long studentId, Long currentUserId);
}
