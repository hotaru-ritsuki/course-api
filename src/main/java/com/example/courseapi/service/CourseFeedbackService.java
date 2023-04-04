package com.example.courseapi.service;

import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.dto.CourseFeedbackDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link CourseFeedback}.
 */
public interface CourseFeedbackService {

    Optional<CourseFeedbackDTO> findById(Long id);

    /**
     * Save a course.
     *
     * @param courseDTO the entity to save.
     * @return the persisted entity.
     */
    CourseFeedbackDTO save(CourseFeedbackDTO courseDTO);

    List<CourseFeedbackDTO> findAll();
    List<CourseFeedbackDTO> findByStudentId(Long studentId);
    List<CourseFeedbackDTO> findByCourseId(Long courseId);

    void delete(Long courseId);

}
