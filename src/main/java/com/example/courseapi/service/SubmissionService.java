package com.example.courseapi.service;

import com.example.courseapi.domain.Submission;
import com.example.courseapi.dto.SubmissionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Submission}.
 */
public interface SubmissionService {

    SubmissionDTO save(SubmissionDTO homeworkDTO);

    List<SubmissionDTO> findAll();

    Optional<SubmissionDTO> findById(Long lessonId, Long studentId);

    void delete(Long lessonId, Long studentId);

    List<Submission> findAllByStudentIdAndCourseId(Long studentId, Long courseId);
}
