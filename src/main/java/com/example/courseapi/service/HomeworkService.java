package com.example.courseapi.service;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.domain.User;
import com.example.courseapi.dto.request.HomeworkRequestDTO;
import com.example.courseapi.dto.response.HomeworkResponseDTO;
import com.example.courseapi.domain.Homework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Homework} entity.
 */
public interface HomeworkService {

    /**
     * Save a homework.
     *
     * @param homeworkRequestDTO the entity to save.
     * @return the persisted entity.
     */
    HomeworkResponseDTO save(final HomeworkRequestDTO homeworkRequestDTO);

    /**
     * Find all homework entities.
     *
     * @param filters  the optional filters to apply when searching for courses
     * @param pageable the pagination information to apply when retrieving the courses
     * @return the list of entities.
     */
    Page<HomeworkResponseDTO> findAll(final Filters filters, final Pageable pageable, final User user);

    /**
     * Find a homework entity by ID.
     *
     * @param homeworkId the ID of the homework to find.
     * @return the optional entity.
     */
    Optional<HomeworkResponseDTO> findById(final Long homeworkId);

    /**
     * Delete a homework entity by ID.
     *
     * @param homeworkId the ID of the homework to delete.
     */
    void delete(final Long homeworkId);

    /**
     * Find all homework entities submitted by a student with the given ID.
     *
     * @param studentId the ID of the student.
     * @return the list of entities.
     */
    List<HomeworkResponseDTO> findByStudent(final Long studentId);

    /**
     * Find all homework entities for a lesson with the given ID.
     *
     * @param lessonId the ID of the lesson.
     * @return the list of entities.
     */
    List<HomeworkResponseDTO> findByLessonId(final Long lessonId);

    /**
     * Upload a homework file for a lesson and a student.
     *
     * @param lessonId  the ID of the lesson.
     * @param file      the homework file.
     * @param studentId the ID of the student.
     * @return the DTO of the uploaded homework.
     */
    HomeworkResponseDTO uploadHomeworkForLesson(final Long lessonId, final MultipartFile file, final Long studentId);

    /**
     * Find all homework entities submitted by a student with the given ID and for a course with the given ID.
     *
     * @param studentId the ID of the student.
     * @param courseId  the ID of the course.
     * @return the list of entities.
     */
    List<Homework> findAllByStudentIdAndCourseId(final Long studentId, final Long courseId);

    byte[] getHomeworkFile(Long lessonId, Long studentId, String identifier);
}
