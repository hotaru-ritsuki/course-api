package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.controller.util.ResponseUtil;
import com.example.courseapi.dto.CourseFeedbackDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.service.CourseFeedbackService;
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
 * REST controller for managing {@link com.example.courseapi.domain.CourseFeedback}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseFeedbackController {
    private static final String ENTITY_NAME = "CourseFeedback";

    private final CourseFeedbackService courseFeedbackService;
    private final EntityHeaderCreator entityHeaderCreator;

    /**
     * {@code POST /course-feedbacks} : Create a new courseFeedback.
     *
     * @param courseFeedbackDTO the courseFeedback to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseFeedbackDTO,
     * or with status {@code 400 (Bad Request)} if the courseFeedback has already an ID.
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-feedbacks")
    public ResponseEntity<CourseFeedbackDTO> createCourseFeedback(@Valid @RequestBody CourseFeedbackDTO courseFeedbackDTO) throws URISyntaxException {
        if (courseFeedbackDTO.getId() != null) {
            throw new SystemException("A new courseFeedback cannot already have an ID", ErrorCode.BAD_REQUEST);
        }
        CourseFeedbackDTO result = courseFeedbackService.save(courseFeedbackDTO);
        return ResponseEntity.created(new URI("/api/course-feedbacks/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /course-feedbacks} : Updates an existing courseFeedback.
     *
     * @param courseFeedbackDTO the courseFeedbackDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseFeedbackDTO,
     * or with status {@code 400 (Bad Request)} if the courseFeedbackDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseFeedbackDTO couldn't be updated.
     */
    @PutMapping("/course-feedbacks")
    public ResponseEntity<CourseFeedbackDTO> updateCourseFeedback(@Valid @RequestBody CourseFeedbackDTO courseFeedbackDTO) {
        if (courseFeedbackDTO.getId() == null) {
            throw new SystemException("Invalid id", ErrorCode.BAD_REQUEST);
        }
        CourseFeedbackDTO result = courseFeedbackService.save(courseFeedbackDTO);
        return ResponseEntity.ok()
                .headers(entityHeaderCreator.createEntityUpdateAlert(ENTITY_NAME, courseFeedbackDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /course-feedbacks} : get all the courseFeedbacks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseFeedbacks in body.
     */
    @GetMapping("/course-feedbacks")
    public List<CourseFeedbackDTO> getAllCourseFeedbacks() {
        return courseFeedbackService.findAll();
    }

    /**
     * {@code GET  /course-feedbacks/:id} : get the "id" courseFeedback.
     *
     * @param id the id of the courseFeedbackDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseFeedbackDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-feedbacks/{id}")
    public ResponseEntity<CourseFeedbackDTO> getCourseFeedback(@PathVariable Long id) {
        Optional<CourseFeedbackDTO> courseFeedbackDTO = courseFeedbackService.findById(id);
        return ResponseUtil.wrapOrNotFound(courseFeedbackDTO);
    }

    /**
     * {@code DELETE  /course-feedbacks/:id} : delete the "id" courseFeedback.
     *
     * @param id the id of the courseFeedbackDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-feedbacks/{id}")
    public ResponseEntity<Void> deleteCourseFeedback(@PathVariable Long id) {
        courseFeedbackService.delete(id);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
                .build();
    }
}
