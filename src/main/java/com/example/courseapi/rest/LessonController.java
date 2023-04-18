package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.controller.util.ResponseUtil;
import com.example.courseapi.dto.LessonDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.service.LessonService;
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
 * REST controller for managing {@link com.example.courseapi.domain.Lesson}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class LessonController {
    private static final String ENTITY_NAME = "Lesson";

    private final LessonService lessonService;
    private final EntityHeaderCreator entityHeaderCreator;

    /**
     * {@code POST /lessons} : Create a new lesson.
     *
     * @param lessonDTO the lesson to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lessonDTO,
     * or with status {@code 400 (Bad Request)} if the lesson has already an ID.
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lessons")
    public ResponseEntity<LessonDTO> createLesson(@Valid @RequestBody LessonDTO lessonDTO) throws URISyntaxException {
        if (lessonDTO.getId() != null) {
            throw new SystemException("A new lesson cannot already have an ID", ErrorCode.BAD_REQUEST);
        }
        LessonDTO result = lessonService.save(lessonDTO);
        return ResponseEntity.created(new URI("/api/lessons/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /lessons} : Updates an existing lesson.
     *
     * @param lessonDTO the lessonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonDTO,
     * or with status {@code 400 (Bad Request)} if the lessonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lessonDTO couldn't be updated.
     */
    @PutMapping("/lessons")
    public ResponseEntity<LessonDTO> updateLesson(@Valid @RequestBody LessonDTO lessonDTO) {
        if (lessonDTO.getId() == null) {
            throw new SystemException("Invalid id", ErrorCode.BAD_REQUEST);
        }
        LessonDTO result = lessonService.save(lessonDTO);
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
    public List<LessonDTO> getAllLessons() {
        return lessonService.findAll();
    }

    /**
     * {@code GET  /lessons/:id} : get the "id" lesson.
     *
     * @param id the id of the lessonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lessonDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lessons/{id}")
    public ResponseEntity<LessonDTO> getLesson(@PathVariable Long id) {
        Optional<LessonDTO> lessonDTO = lessonService.findById(id);
        return ResponseUtil.wrapOrNotFound(lessonDTO);
    }

    /**
     * {@code DELETE  /lessons/:id} : delete the "id" lesson.
     *
     * @param id the id of the lessonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.delete(id);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
                .build();
    }
}
