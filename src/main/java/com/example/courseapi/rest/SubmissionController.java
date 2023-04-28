package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.util.ResponseUtil;
import com.example.courseapi.domain.Instructor;
import com.example.courseapi.domain.User;
import com.example.courseapi.dto.GradeDTO;
import com.example.courseapi.dto.SubmissionDTO;
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
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubmissionController {
    private static final String ENTITY_NAME = "Submission";

    private final SubmissionService submissionService;
    private final EntityHeaderCreator entityHeaderCreator;

    /**
     * {@code PUT  /submissions} : Updates an existing submission.
     *
     * @param submissionDTO the submissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submissionDTO,
     * or with status {@code 400 (Bad Request)} if the submissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the submissionDTO couldn't be updated.
     */
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @RequestMapping(value = "/submissions", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<SubmissionDTO> updateSubmission(
            @Valid @RequestBody SubmissionDTO submissionDTO, @CurrentUser Instructor instructor) throws URISyntaxException {
        SubmissionDTO result = submissionService.save(submissionDTO, instructor.getId());
        return ResponseEntity.created(
                new URI("/api/lesson/" + result.getLessonId() + "/student/" + result.getStudentId() + "/submission" ))
                .headers(entityHeaderCreator.createEntityUpdateAlert(ENTITY_NAME, submissionDTO.getEmbeddedIdsString()))
                .body(result);
    }

    /**
     * {@code GET  /submissions} : get all the submissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of submissions in body.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @GetMapping("/lesson/{lessonId}/submissions")
    public ResponseEntity<List<SubmissionDTO>> getAllSubmissionsForLesson(@PathVariable Long lessonId, @CurrentUser User currentUser) {
        return ResponseEntity.ok(submissionService.findAllByLesson(lessonId, currentUser.getId()));
    }

    /**
     * {@code GET  /submissions} : get all the submissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of submissions in body.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @GetMapping("/student/{studentId}/submissions")
    public ResponseEntity<List<SubmissionDTO>> getAllSubmissionsForStudent(@PathVariable Long studentId, @CurrentUser User currentUser) {
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
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @GetMapping("/lesson/{lessonId}/student/{studentId}/submission")
    public ResponseEntity<SubmissionDTO> getSubmission(@PathVariable Long lessonId, @PathVariable Long studentId) {
        Optional<SubmissionDTO> submissionDTO = submissionService.findById(lessonId, studentId);
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
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/lesson/{lessonId}/student/{studentId}/submission")
    public ResponseEntity<SubmissionDTO> createSubmission(
            @PathVariable Long lessonId, @PathVariable Long studentId,
            @Valid @RequestBody GradeDTO gradeDTO, @CurrentUser Instructor instructor
    ) throws URISyntaxException {
        SubmissionDTO result = submissionService.saveGrade(lessonId, studentId, gradeDTO.getGrade(), instructor.getId());
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
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @DeleteMapping("/lesson/{lessonId}/student/{studentId}/submission")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long lessonId, @PathVariable Long studentId) {
        submissionService.delete(lessonId, studentId);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, String.format("Lesson:%s.Student:%s", lessonId, studentId)))
                .build();
    }
}
