package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.dto.request.CourseRequestDTO;
import com.example.courseapi.dto.request.LessonsUpdateDTO;
import com.example.courseapi.dto.response.CourseResponseDTO;
import com.example.courseapi.dto.response.CourseStatusResponseDTO;
import com.example.courseapi.util.ResponseUtil;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.User;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.security.annotation.CurrentUser;
import com.example.courseapi.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing {@link com.example.courseapi.domain.Course}.
 */
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseController {
    private static final String ENTITY_NAME = "Course";

    private final CourseService courseService;
    private final EntityHeaderCreator entityHeaderCreator;

    /**
     * {@code POST /courses} : Create a new course.
     *
     * @param courseDTO the course to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseDTO,
     * or with status {@code 400 (Bad Request)} if the course has already an ID.
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@accessValidator.courseAccess(#courseDTO.id, #courseDTO.instructorIds)")
    @PostMapping("/courses")
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO courseDTO) throws URISyntaxException {
        if (courseDTO.getId() != null) {
            throw new SystemException("A new course cannot already have an ID", ErrorCode.BAD_REQUEST);
        }
        CourseResponseDTO result = courseService.save(courseDTO);
        return ResponseEntity.created(new URI("/api/v1/courses/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /courses} : Updates an existing course.
     *
     * @param courseDTO the courseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseDTO,
     * or with status {@code 400 (Bad Request)} if the courseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseDTO couldn't be updated.
     */
    @PreAuthorize("@accessValidator.courseAccess(#courseDTO.id, #courseDTO.instructorIds)")
    @PutMapping("/courses")
    public ResponseEntity<CourseResponseDTO> updateCourse(@Valid @RequestBody CourseRequestDTO courseDTO) {
        if (courseDTO.getId() == null) {
            throw new SystemException("Invalid id provided for a course", ErrorCode.BAD_REQUEST);
        }
        CourseResponseDTO result = courseService.save(courseDTO);
        return ResponseEntity.ok()
                .headers(entityHeaderCreator.createEntityUpdateAlert(ENTITY_NAME, courseDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /courses} : get all the courses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courses in body.
     */
    @GetMapping("/courses")
    public Page<CourseResponseDTO> getAllCourses(final Filters filters, final Pageable pageable, @CurrentUser User user) {
        return courseService.findAll(filters, pageable, user);
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the courseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable Long id) {
        Optional<CourseResponseDTO> courseDTO = courseService.findById(id);
        return ResponseUtil.wrapOrNotFound(courseDTO);
    }

    /**
     * {@code PATCH  /courses/:id} : get the "id" course.
     *
     * @param courseId the id of the courseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("@accessValidator.courseAccess(#courseId, null)")
    @PutMapping("/courses/{courseId}/lessons")
    public ResponseEntity<CourseResponseDTO> updateCourseLessons(@PathVariable Long courseId, @Valid @RequestBody LessonsUpdateDTO lessonsDTO) {
        CourseResponseDTO courseDTO = courseService.updateCourseLessons(courseId, lessonsDTO);
        return ResponseEntity.ok(courseDTO);
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" course.
     *
     * @param courseId the id of the courseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("@accessValidator.courseAccess(#courseId, null)")
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.delete(courseId);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, courseId.toString()))
                .build();
    }

    /**
     * {@code POST /courses/{courseId}/subscribe} : Subscribes current student to a course.
     *
     * @param courseId the corresponding course id to subscribe.
     * @param student current student which needs to be subscribed
     * @return the {@link ResponseEntity} with status {@code 200 (OK)},
     * or with status {@code 400 (Bad Request)} if the student or course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the student couldn't be updated.
     */
    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/courses/{courseId}/subscribe")
    public ResponseEntity<Void> subscribeMeToCourse(@PathVariable Long courseId, @CurrentUser User student) {
        courseService.subscribeStudentToCourse(courseId, student.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * {@code POST /courses/{courseId}/subscribe/{studentId}} : Subscribes student to a course.
     *
     * @param courseId the corresponding course id to subscribe.
     * @param studentId the student id which needs to be subscribed.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)},
     * or with status {@code 400 (Bad Request)} if the course has already an ID.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/courses/{courseId}/subscribe/{studentId}")
    public ResponseEntity<Void> subscribeStudentToCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        courseService.subscribeStudentToCourse(courseId, studentId);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code GET  /courses/my} : get my courses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseDTOs,
     * or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")
    @GetMapping("/courses/my")
    public ResponseEntity<Collection<? extends CourseResponseDTO>> getMyCourses(@CurrentUser User user) {
        Set<? extends CourseResponseDTO> courseDTOs = courseService.getMyCourses(user.getId());
        return ResponseEntity.ok(courseDTOs);
    }


    /**
     * {@code GET  /courses/{courseId}/status} : get the course status for requested id.
     *
     * @param courseId the id of the courseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/courses/{courseId}/status")
    public ResponseEntity<CourseStatusResponseDTO> getCourseStatus(@PathVariable Long courseId, @CurrentUser Student student) {
        CourseStatusResponseDTO courseStatusDTO = courseService.getCourseStatus(courseId, student.getId());
        return ResponseEntity.ok(courseStatusDTO);
    }

    /**
     * {@code GET  /courses/{courseId}/status} : get the course status for requested course id and student id.
     *
     * @param courseId the id of the courseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("@accessValidator.courseAccess(#courseId, null)")
    @GetMapping("/courses/{courseId}/students/{studentId}/status")
    public ResponseEntity<CourseStatusResponseDTO> getCourseStatus(@PathVariable Long courseId, @PathVariable Long studentId) {
        CourseStatusResponseDTO courseStatusDTO = courseService.getCourseStatus(courseId, studentId);
        return ResponseEntity.ok(courseStatusDTO);
    }

    /**
     * {@code PUT /courses/{courseId}/instructor/{instructorId}} : Adds instructor to a course
     *
     * @param courseId the course id to set instructor.
     * @param instructorId the instructor id which needs to be set.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)},
     * or with status {@code 400 (Bad Request)} if the course has already an ID.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/courses/{courseId}/instructor/{instructorId}")
    public ResponseEntity<Void> addInstructorToCourse(@PathVariable Long courseId, @PathVariable Long instructorId) {
        courseService.addInstructorToCourse(courseId, instructorId);
        return ResponseEntity.ok()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, courseId.toString()))
                .build();
    }

    /**
     * {@code DELETE  /courses/{courseId}/instructor/{instructorId}} : Deletes instructor from a course.
     *
     * @param courseId the course id to delete instructor.
     * @param instructorId the instructor id which needs to be deleted.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/courses/{courseId}/instructor/{instructorId}")
    public ResponseEntity<Void> deleteInstructorForCourse(@PathVariable Long courseId, @PathVariable Long instructorId) {
        courseService.deleteInstructorForCourse(courseId, instructorId);
        return ResponseEntity.ok()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, courseId.toString()))
                .build();
    }

}
