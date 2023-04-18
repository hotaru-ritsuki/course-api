package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.controller.util.ResponseUtil;
import com.example.courseapi.dto.SubmissionDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.example.courseapi.domain.Submission}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubmissionController {
    private static final String ENTITY_NAME = "Submission";

    private final SubmissionService submissionService;
    private final EntityHeaderCreator entityHeaderCreator;

    /**
     * {@code POST /submissions} : Create a new submission.
     *
     * @param submissionDTO the submission to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new submissionDTO,
     * or with status {@code 400 (Bad Request)} if the submission has already an ID.
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/submissions")
    public ResponseEntity<SubmissionDTO> createSubmission(@Valid @RequestBody SubmissionDTO submissionDTO) throws URISyntaxException {
        SubmissionDTO result = submissionService.save(submissionDTO);
        return ResponseEntity.created(new URI("/api/submissions/" + result.getLessonId() + "/" + result.getStudentId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getEmbeddedIdsString()))
                .body(result);
    }

    /**
     * {@code PUT  /submissions} : Updates an existing submission.
     *
     * @param submissionDTO the submissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submissionDTO,
     * or with status {@code 400 (Bad Request)} if the submissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the submissionDTO couldn't be updated.
     */
    @PutMapping("/submissions")
    public ResponseEntity<SubmissionDTO> updateSubmission(@Valid @RequestBody SubmissionDTO submissionDTO) {
        if (submissionDTO.getStudentId() == null || submissionDTO.getLessonId() == null) {
            throw new SystemException("Invalid ids provided", ErrorCode.BAD_REQUEST);
        }
        SubmissionDTO result = submissionService.save(submissionDTO);
        return ResponseEntity.ok()
                .headers(entityHeaderCreator.createEntityUpdateAlert(ENTITY_NAME, submissionDTO.getEmbeddedIdsString()))
                .body(result);
    }

    /**
     * {@code GET  /submissions} : get all the submissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of submissions in body.
     */
    @GetMapping("/submissions")
    public List<SubmissionDTO> getAllSubmissions() {
        return submissionService.findAll();
    }

    /**
     * {@code GET  /submissions/:studentId/:lessonId} : get the "id" submission.
     *
     * @param lessonId the lesson id of the submissionDTO to retrieve.
     * @param studentId the student id of the submissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the submissionDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/submissions/{lessonId}/{studentId}")
    public ResponseEntity<SubmissionDTO> getSubmission(@PathVariable Long lessonId, @PathVariable Long studentId) {
        Optional<SubmissionDTO> submissionDTO = submissionService.findById(lessonId, studentId);
        return ResponseUtil.wrapOrNotFound(submissionDTO);
    }

    /**
     * {@code DELETE  /submissions/:studentId/:lessonId} : delete the "id" submission.
     *
     * @param lessonId the lesson id of the submissionDTO to delete.
     * @param studentId the student id of the submissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/submissions/{lessonId}/{studentId}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long lessonId, @PathVariable Long studentId) {
        submissionService.delete(lessonId, studentId);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, String.format("Lesson:%s.Student:%s", lessonId, studentId)))
                .build();
    }
}
