package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.controller.util.ResponseUtil;
import com.example.courseapi.dto.CourseDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.example.courseapi.domain.Course}.
 */
@RestController
@RequiredArgsConstructor
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
    @PostMapping("/courses")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) throws URISyntaxException {
        if (courseDTO.getId() != null) {
            throw new SystemException("A new course cannot already have an ID", ErrorCode.BAD_REQUEST);
        }
        CourseDTO result = courseService.save(courseDTO);
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
    @PutMapping("/courses")
    public ResponseEntity<CourseDTO> updateCourse(@Valid @RequestBody CourseDTO courseDTO) {
        if (courseDTO.getId() == null) {
            throw new SystemException("Invalid id", ErrorCode.BAD_REQUEST);
        }
        CourseDTO result = courseService.save(courseDTO);
        return ResponseEntity.ok()
                .headers(entityHeaderCreator.createEntityUpdateAlert(ENTITY_NAME, courseDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /courses} : get all the courses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courses in body.
     */
    @GetMapping("/courses")
    public List<CourseDTO> getAllCourses() {
        return courseService.findAll();
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the courseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable Long id) {
        Optional<CourseDTO> courseDTO = courseService.findById(id);
        return ResponseUtil.wrapOrNotFound(courseDTO);
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" course.
     *
     * @param id the id of the courseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
                .build();
    }
}
