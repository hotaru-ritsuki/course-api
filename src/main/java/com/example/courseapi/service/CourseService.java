package com.example.courseapi.service;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.User;
import com.example.courseapi.dto.*;
import com.example.courseapi.dto.request.LessonsUpdateDTO;
import com.example.courseapi.dto.response.CourseResponseDTO;
import com.example.courseapi.dto.response.CourseStatusResponseDTO;
import com.example.courseapi.dto.request.CourseRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service interface for managing {@link Course} entity.
 */
public interface CourseService {

    /**
     * Finds a course by its ID.
     *
     * @param id the ID of the course to find
     * @return an optional containing the found course DTO, or empty if not found
     */
    Optional<CourseResponseDTO> findById(final Long id);

    /**
     * Saves a new course or updates an existing one.
     *
     * @param courseDTO the course DTO to save or update
     * @return the saved or updated course DTO
     */
    CourseResponseDTO save(final CourseRequestDTO courseDTO);

    /**
     * Finds all courses with optional filters and pagination.
     *
     * @param filters  the optional filters to apply when searching for courses
     * @param pageable the pagination information to apply when retrieving the courses
     * @return a page containing the found course DTOs
     */
    Page<CourseResponseDTO> findAll(final Filters filters, final Pageable pageable, final User user);

    /**
     * Finds all courses associated with a specific student ID.
     *
     * @param studentId the ID of the student to find courses for
     * @return a list containing the found course DTOs
     */
    List<CourseResponseDTO> findByStudentId(final Long studentId);

    /**
     * Deletes a course with the given ID.
     *
     * @param courseId the ID of the course to delete
     */
    void delete(final Long courseId);

    /**
     * Subscribes a student to a course.
     *
     * @param courseId  the ID of the course to subscribe the student to
     * @param studentId the ID of the student to subscribe to the course
     */
    void subscribeStudentToCourse(final Long courseId, final Long studentId);

    /**
     * Retrieves the course status for a student in a specific course.
     *
     * @param courseId  the ID of the course to retrieve the status for
     * @param studentId the ID of the student to retrieve the status for
     * @return the course status DTO for the given course and student
     */
    CourseStatusResponseDTO getCourseStatus(final Long courseId, final Long studentId);

    /**
     * Calculates the course grade for a student in a specific course.
     *
     * @param studentId the ID of the student to calculate the grade for
     * @param course    the course to calculate the grade for
     * @return the course grade DTO for the given student and course
     */
    CourseGradeDTO calculateCourseStatus(final Long studentId, final Course course);

    /**
     * Calculates the course grades for a set of courses for a specific student.
     *
     * @param studentId the ID of the student to calculate the grades for
     * @param courses   the set of courses to calculate the grades for
     * @return a set of course grade DTOs for the given student and courses
     */
    Set<CourseGradeDTO> calculateCourseStatus(final Long studentId, final Set<Course> courses);

    /**
     * Checks if a student is subscribed to a specific course.
     *
     * @param courseId  the ID of the course to check for subscription
     * @param studentId the ID of the student to check for subscription
     * @return true if the student is subscribed to the course, false otherwise
     */
    boolean isStudentSubscribedToCourse(final Long courseId, final Long studentId);

    /**
     * Retrieves all courses associated with a specific user ID.
     *
     * @param userId the ID of the user to retrieve courses for
     * @return a set of course DTOs associated with the given user ID
     */
    Set<? extends CourseResponseDTO> getMyCourses(final Long userId);

    /**
     * Adds an instructor with the given ID to the course with the given ID.
     *
     * @param courseId     the ID of the course.
     * @param instructorId the ID of the instructor to be added.
     * @return returns response DTO of the course.
     */
    CourseResponseDTO addInstructorToCourse(final Long courseId, final Long instructorId);

    /**
     * Deletes an instructor with the given ID from the course with the given ID.
     *
     * @param courseId     the ID of the course.
     * @param instructorId the ID of the instructor to be deleted.
     * @return returns response DTO of the course.
     */
    CourseResponseDTO deleteInstructorForCourse(final Long courseId, final Long instructorId);

    CourseResponseDTO updateCourseLessonsAndSave(final Long courseId, final LessonsUpdateDTO lessonsDTO);
}
