package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.dto.request.SubmissionRequestDTO;
import com.example.courseapi.dto.response.SubmissionResponseDTO;
import com.example.courseapi.util.ResponseUtil;
import com.example.courseapi.domain.Instructor;
import com.example.courseapi.domain.User;
import com.example.courseapi.dto.request.GradeDTO;
import com.example.courseapi.security.annotation.CurrentUser;
import com.example.courseapi.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubmissionController {
    private static final String ENTITY_NAME = "Submission";

    private final SubmissionService submissionService;
    private final EntityHeaderCreator entityHeaderCreator;

    /**
     * {@code PUT  /submissions} : Updates an existing submission.
     *
     * @param submissionRequestDTO the submissionResponseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submissionResponseDTO,
     * or with status {@code 400 (Bad Request)} if the submissionResponseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the submissionResponseDTO couldn't be updated.
     */
    @PreAuthorize("@accessValidator.submissionAccess(#submissionRequestDTO.lessonId)")
    @RequestMapping(value = "/submissions", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<SubmissionResponseDTO> updateSubmission(
            @Valid @RequestBody SubmissionRequestDTO submissionRequestDTO) throws URISyntaxException {
        SubmissionResponseDTO submissionResponseDTO = submissionService.save(submissionRequestDTO);
        return ResponseEntity.created(
                new URI("/api/lesson/" + submissionResponseDTO.getLessonId() + "/student/" + submissionResponseDTO.getStudentId() + "/submission" ))
                .headers(entityHeaderCreator.createEntityUpdateAlert(ENTITY_NAME, submissionResponseDTO.getEmbeddedIdsString()))
                .body(submissionResponseDTO);
    }

    /**
     * {@code GET  /submissions} : get all the submissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of submissions in body.
     */
    @PreAuthorize("@accessValidator.submissionAccess(#lessonId)")
    @GetMapping("/lesson/{lessonId}/submissions")
    public ResponseEntity<List<SubmissionResponseDTO>> getAllSubmissionsForLesson(@PathVariable Long lessonId, @CurrentUser User currentUser) {
        return ResponseEntity.ok(submissionService.findAllByLesson(lessonId, currentUser.getId()));
    }

    /**
     * {@code GET  /submissions} : get all the submissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of submissions in body.
     */
    @GetMapping("/student/{studentId}/submissions")
    public ResponseEntity<List<SubmissionResponseDTO>> getAllSubmissionsForStudent(@PathVariable Long studentId, @CurrentUser User currentUser) {
        return ResponseEntity.ok(submissionService.findAllByStudent(studentId, currentUser.getId()));
    }

    /**
     * {@code GET  /submissions/:studentId/:lessonId} : get the "id" submission.
     *
     * @param lessonId the lesson id of the submissionDTO to retrieve.
     * @param studentId the student id of the submissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the submissionDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("@accessValidator.submissionAccess(#lessonId)")
    @GetMapping("/lesson/{lessonId}/student/{studentId}/submission")
    public ResponseEntity<SubmissionResponseDTO> getSubmission(@PathVariable Long lessonId, @PathVariable Long studentId) {
        Optional<SubmissionResponseDTO> submissionDTO = submissionService.findById(lessonId, studentId);
        return ResponseUtil.wrapOrNotFound(submissionDTO);
    }

    /**
     * {@code POST /submissions} : Create a new submission.
     *
     * @param gradeDTO the submission to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new submissionDTO,
     * or with status {@code 400 (Bad Request)} if the submission has already an ID.
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@accessValidator.submissionAccess(#lessonId)")
    @PostMapping("/lesson/{lessonId}/student/{studentId}/submission")
    public ResponseEntity<SubmissionResponseDTO> createSubmission(
            @PathVariable Long lessonId, @PathVariable Long studentId,
            @Valid @RequestBody GradeDTO gradeDTO
    ) throws URISyntaxException {
        SubmissionResponseDTO result = submissionService.saveGrade(lessonId, studentId, gradeDTO.getGrade());
        return ResponseEntity.created(new URI("/api/submissions/" + result.getLessonId() + "/" + result.getStudentId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getEmbeddedIdsString()))
                .body(result);
    }

    /**
     * {@code DELETE  /submissions/:studentId/:lessonId} : delete the "id" submission.
     *
     * @param lessonId the lesson id of the submissionDTO to delete.
     * @param studentId the student id of the submissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("@accessValidator.submissionAccess(#lessonId)")
    @DeleteMapping("/lesson/{lessonId}/student/{studentId}/submission")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long lessonId, @PathVariable Long studentId) {
        submissionService.delete(lessonId, studentId);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, String.format("Lesson:%s.Student:%s", lessonId, studentId)))
                .build();
    }
}
