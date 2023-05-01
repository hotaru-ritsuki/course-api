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
     * @param courseFeedbackRequestDTO the courseFeedback to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseFeedbackResponseDTO,
     * or with status {@code 400 (Bad Request)} if the courseFeedback has already an ID.
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@accessValidator.courseFeedbackAccess(#courseFeedbackRequestDTO.courseId)")
    @PostMapping("/course-feedbacks")
    public ResponseEntity<CourseFeedbackResponseDTO> createCourseFeedback(@Valid @RequestBody CourseFeedbackRequestDTO courseFeedbackRequestDTO,
                                                                          @CurrentUser Student student) throws URISyntaxException {
        if (courseFeedbackRequestDTO.getId() != null) {
            throw new SystemException("A new courseFeedback cannot already have an ID", ErrorCode.BAD_REQUEST);
        }
        CourseFeedbackResponseDTO result = courseFeedbackService.save(courseFeedbackRequestDTO, student);
        return ResponseEntity.created(new URI("/api/course-feedbacks/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /course-feedbacks} : Updates an existing courseFeedback.
     *
     * @param courseFeedbackRequestDTO the courseFeedbackResponseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseFeedbackResponseDTO,
     * or with status {@code 400 (Bad Request)} if the courseFeedbackResponseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseFeedbackResponseDTO couldn't be updated.
     */
    @PreAuthorize("@accessValidator.courseFeedbackAccess(#courseFeedbackRequestDTO.courseId)")
    @PutMapping("/course-feedbacks")
    public ResponseEntity<CourseFeedbackResponseDTO> updateCourseFeedback(@Valid @RequestBody CourseFeedbackRequestDTO courseFeedbackRequestDTO,
                                                                          @CurrentUser Student student) {
        if (courseFeedbackRequestDTO.getId() == null) {
            throw new SystemException("Invalid id", ErrorCode.BAD_REQUEST);
        }
        CourseFeedbackResponseDTO result = courseFeedbackService.save(courseFeedbackRequestDTO, student);
        return ResponseEntity.ok()
                .headers(entityHeaderCreator.createEntityUpdateAlert(ENTITY_NAME, courseFeedbackRequestDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /course-feedbacks} : get all the courseFeedbacks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseFeedbacks in body.
     */
    @GetMapping("/course-feedbacks")
    public Page<CourseFeedbackResponseDTO> getAllCourseFeedbacks(Filters filters, Pageable pageable) {
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
    public ResponseEntity<CourseFeedbackResponseDTO> getCourseFeedback(@PathVariable Long courseFeedbackId) {
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
    public ResponseEntity<Void> deleteCourseFeedback(@PathVariable Long courseFeedbackId) {
        courseFeedbackService.delete(courseFeedbackId);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, courseFeedbackId.toString()))
                .build();
    }
}
