package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.domain.User;
import com.example.courseapi.dto.request.LessonRequestDTO;
import com.example.courseapi.dto.response.LessonResponseDTO;
import com.example.courseapi.security.annotation.CurrentUser;
import com.example.courseapi.util.ResponseUtil;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.service.LessonService;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.example.courseapi.domain.Lesson}.
 */
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
     * @param lessonRequestDTO the lesson to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lessonResponseDTO,
     * or with status {@code 400 (Bad Request)} if the lesson has already an ID.
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@accessValidator.lessonAccess(#lessonRequestDTO.courseId)")
    @PostMapping("/lessons")
    public ResponseEntity<LessonResponseDTO> createLesson(@Valid @RequestBody LessonRequestDTO lessonRequestDTO) throws URISyntaxException {
        if (lessonRequestDTO.getId() != null) {
            throw new SystemException("A new lesson cannot already have an ID", ErrorCode.BAD_REQUEST);
        }
        LessonResponseDTO result = lessonService.save(lessonRequestDTO);
        return ResponseEntity.created(new URI("/api/lessons/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /lessons} : Updates an existing lesson.
     *
     * @param lessonRequestDTO the lessonResponseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonResponseDTO,
     * or with status {@code 400 (Bad Request)} if the lessonResponseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lessonResponseDTO couldn't be updated.
     */
    @PreAuthorize("@accessValidator.lessonAccess(#lessonRequestDTO.courseId)")
    @PutMapping("/lessons")
    public ResponseEntity<LessonResponseDTO> updateLesson(@Valid @RequestBody LessonRequestDTO lessonRequestDTO) {
        if (lessonRequestDTO.getId() == null) {
            throw new SystemException("Invalid id", ErrorCode.BAD_REQUEST);
        }
        LessonResponseDTO result = lessonService.save(lessonRequestDTO);
        return ResponseEntity.ok()
                .headers(entityHeaderCreator.createEntityUpdateAlert(ENTITY_NAME, lessonRequestDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /lessons} : get all the lessons.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessons in body.
     */
    @GetMapping("/lessons")
    public Page<LessonResponseDTO> getAllLessons(Filters filters, Pageable pageable) {
        return lessonService.findAll(filters, pageable);
    }

    /**
     * {@code GET  /lessons/:id} : get the "id" lesson.
     *
     * @param id the id of the lessonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lessonDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lessons/{id}")
    public ResponseEntity<LessonResponseDTO> getLesson(@PathVariable Long id) {
        Optional<LessonResponseDTO> lessonDTO = lessonService.findById(id);
        return ResponseUtil.wrapOrNotFound(lessonDTO);
    }

    /**
     * {@code DELETE  /lessons/:id} : delete the "id" lesson.
     *
     * @param id the id of the lessonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.delete(id);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
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
    public ResponseEntity<List<LessonResponseDTO>> getLessonsForCourse(@PathVariable Long courseId) {
        List<LessonResponseDTO> lessonResponseDTOS = lessonService.findByCourseId(courseId);
        return ResponseEntity.ok(lessonResponseDTOS);
    }
}
