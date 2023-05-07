package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.domain.Student;
import com.example.courseapi.dto.request.CourseFeedbackRequestDTO;
import com.example.courseapi.dto.response.CourseFeedbackResponseDTO;
import com.example.courseapi.security.annotation.CurrentUser;
import com.example.courseapi.util.ResponseUtil;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.service.CourseFeedbackService;
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
import java.util.Optional;

/**
 * REST controller for managing {@link com.example.courseapi.domain.CourseFeedback}.
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseFeedbackController {
    private static final String ENTITY_NAME = "CourseFeedback";

    private final CourseFeedbackService courseFeedbackService;
    private final EntityHeaderCreator entityHeaderCreator;

    /**
     * {@code POST /course-feedbacks} : Create a new courseFeedback.
     *
     * @param courseFeedbackDTO the courseFeedback to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseFeedbackResponseDTO,
     * or with status {@code 400 (Bad Request)} if the courseFeedback has already an ID.
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@accessValidator.courseFeedbackAccess(#courseFeedbackDTO.courseId)")
    @PostMapping("/course-feedbacks")
    public ResponseEntity<CourseFeedbackResponseDTO> createCourseFeedback(
            @Valid @RequestBody final CourseFeedbackRequestDTO courseFeedbackDTO,
            @CurrentUser final Student student
    ) throws URISyntaxException {
        log.debug("REST POST request to save course feedback : {}", courseFeedbackDTO);
        if (courseFeedbackDTO.getId() != null) {
            throw new SystemException("A new courseFeedback cannot already have an ID", ErrorCode.BAD_REQUEST);
        }
        CourseFeedbackResponseDTO result = courseFeedbackService.save(courseFeedbackDTO, student.getId());
        return ResponseEntity.created(new URI("/api/course-feedbacks/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /course-feedbacks} : Updates an existing courseFeedback.
     *
     * @param courseFeedbackDTO the courseFeedbackResponseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseFeedbackResponseDTO,
     * or with status {@code 400 (Bad Request)} if the courseFeedbackResponseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseFeedbackResponseDTO couldn't be updated.
     */
    @PreAuthorize("@accessValidator.courseFeedbackAccess(#courseFeedbackDTO.courseId)")
    @PutMapping("/course-feedbacks")
    public ResponseEntity<CourseFeedbackResponseDTO> updateCourseFeedback(
            @Valid @RequestBody final CourseFeedbackRequestDTO courseFeedbackDTO, @CurrentUser final Student student) {
        log.debug("REST PUT request to update course feedback : {}", courseFeedbackDTO);
        if (courseFeedbackDTO.getId() == null) {
            throw new SystemException("Invalid id", ErrorCode.BAD_REQUEST);
        }
        CourseFeedbackResponseDTO result = courseFeedbackService.save(courseFeedbackDTO, student.getId());
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
    public Page<CourseFeedbackResponseDTO> getAllCourseFeedbacks(final Filters filters, final Pageable pageable) {
        log.debug("REST GET request to get a page of course feedbacks");
        return courseFeedbackService.findAll(filters, pageable);
    }

    /**
     * {@code GET  /course-feedbacks/:id} : get the "id" courseFeedback.
     *
     * @param courseFeedbackId the id of the courseFeedbackDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseFeedbackDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-feedbacks/{courseFeedbackId}")
    public ResponseEntity<CourseFeedbackResponseDTO> getCourseFeedback(@PathVariable final Long courseFeedbackId) {
        log.debug("REST GET request to get course feedback with id: {}", courseFeedbackId);
        Optional<CourseFeedbackResponseDTO> courseFeedbackDTO = courseFeedbackService.findById(courseFeedbackId);
        return ResponseUtil.wrapOrNotFound(courseFeedbackDTO);
    }

    /**
     * {@code DELETE  /course-feedbacks/:id} : delete the "id" courseFeedback.
     *
     * @param courseFeedbackId the id of the courseFeedbackDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/course-feedbacks/{courseFeedbackId}")
    public ResponseEntity<Void> deleteCourseFeedback(@PathVariable final Long courseFeedbackId) {
        log.debug("REST DELETE request to delete course feedback with id: {}", courseFeedbackId);
        courseFeedbackService.delete(courseFeedbackId);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, courseFeedbackId.toString()))
                .build();
    }
}
