package com.example.courseapi.service;

import com.example.courseapi.domain.Course;
import com.example.courseapi.dto.CourseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Course}.
 */
public interface CourseService {

    Optional<CourseDTO> findById(Long id);

    /**
     * Save a course.
     *
     * @param courseDTO the entity to save.
     * @return the persisted entity.
     */
    CourseDTO save(CourseDTO courseDTO);

    List<CourseDTO> findAll();
    List<CourseDTO> findByStudentId(Long studentId);

    void delete(Long courseId);

}
