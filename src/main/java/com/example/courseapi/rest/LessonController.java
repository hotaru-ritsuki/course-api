package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.dto.request.LessonRequestDTO;
import com.example.courseapi.dto.response.LessonResponseDTO;
import com.example.courseapi.util.ResponseUtil;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.example.courseapi.domain.Lesson}.
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class LessonController {
    private static final String ENTITY_NAME = "Lesson";

    private final LessonService lessonService;
    private final EntityHeaderCreator entityHeaderCreator;

    /**
     * {@code POST /lessons} : Create a new lesson.
     *
     * @param lessonDTO the lesson to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lessonResponseDTO,
     * or with status {@code 400 (Bad Request)} if the lesson has already an ID.
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@accessValidator.lessonAccess(#lessonDTO.courseId)")
    @PostMapping("/lessons")
    public ResponseEntity<LessonResponseDTO> createLesson(@Valid @RequestBody final LessonRequestDTO lessonDTO)
            throws URISyntaxException {
        log.debug("REST POST request to save lesson : {}", lessonDTO);
        if (lessonDTO.getId() != null) {
            throw new SystemException("A new lesson cannot already have an ID", ErrorCode.BAD_REQUEST);
        }
        LessonResponseDTO result = lessonService.save(lessonDTO);
        return ResponseEntity.created(new URI("/api/lessons/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /lessons} : Updates an existing lesson.
     *
     * @param lessonDTO the lessonResponseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonResponseDTO,
     * or with status {@code 400 (Bad Request)} if the lessonResponseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lessonResponseDTO couldn't be updated.
     */
    @PreAuthorize("@accessValidator.lessonAccess(#lessonDTO.courseId)")
    @PutMapping("/lessons")
    public ResponseEntity<LessonResponseDTO> updateLesson(@Valid @RequestBody final LessonRequestDTO lessonDTO) {
        log.debug("REST PUT request to update lesson : {}", lessonDTO);
        if (lessonDTO.getId() == null) {
            throw new SystemException("Invalid id", ErrorCode.BAD_REQUEST);
        }
        LessonResponseDTO result = lessonService.save(lessonDTO);
        return ResponseEntity.ok()
                .headers(entityHeaderCreator.createEntityUpdateAlert(ENTITY_NAME, lessonDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /lessons} : get all the lessons.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessons in body.
     */
    @GetMapping("/lessons")
    public Page<LessonResponseDTO> getAllLessons(final Filters filters, final Pageable pageable) {
        log.debug("REST GET request to get a page of lessons");
        return lessonService.findAll(filters, pageable);
    }

    /**
     * {@code GET  /lessons/:id} : get the "id" lesson.
     *
     * @param lessonId the id of the lessonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lessonDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<LessonResponseDTO> getLesson(@PathVariable final Long lessonId) {
        log.debug("REST GET request to get lesson with id: {}", lessonId);
        Optional<LessonResponseDTO> lessonDTO = lessonService.findById(lessonId);
        return ResponseUtil.wrapOrNotFound(lessonDTO);
    }

    /**
     * {@code DELETE  /lessons/:id} : delete the "id" lesson.
     *
     * @param lessonId the id of the lessonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/lessons/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable final Long lessonId) {
        log.debug("REST DELETE request to delete lesson with id: {}", lessonId);
        lessonService.delete(lessonId);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, lessonId.toString()))
                .build();
    }

    /**
     * {@code GET  /lessons/:id} : get the "id" lesson.
     *
     * @param courseId the id of the lessonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lessonDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{courseId}/lessons")
    public ResponseEntity<List<LessonResponseDTO>> getLessonsForCourse(@PathVariable final Long courseId) {
        log.debug("REST GET request to get all lessons for course with id: {}", courseId);
        List<LessonResponseDTO> lessonResponseDTOS = lessonService.findByCourseId(courseId);
        return ResponseEntity.ok(lessonResponseDTOS);
    }
}
