package com.example.courseapi.service;

import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.enums.CourseStatus;
import com.example.courseapi.dto.CourseDTO;
import com.example.courseapi.dto.CourseStatusDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    void subscribeStudentToCourse(Long courseId, Long studentId);

    CourseStatusDTO getCourseStatus(Long courseId, Long studentId);

    CourseStatus calculateCourseStatus(Long studentId, Course course);

    boolean isStudentSubscribedToCourse(Long courseId, Long studentId);

    Set<CourseDTO> getMyCourses(Long userId);
}
