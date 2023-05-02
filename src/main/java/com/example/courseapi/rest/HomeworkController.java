package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.domain.User;
import com.example.courseapi.dto.request.HomeworkRequestDTO;
import com.example.courseapi.security.annotation.CurrentUser;
import com.example.courseapi.util.ResponseUtil;
import com.example.courseapi.domain.Student;
import com.example.courseapi.dto.response.HomeworkResponseDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.service.HomeworkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * REST controller for managing {@link com.example.courseapi.domain.Homework}.
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class HomeworkController {
    private static final String ENTITY_NAME = "Homework";

    private final HomeworkService homeworkService;
    private final EntityHeaderCreator entityHeaderCreator;

    /**
     * {@code POST /homeworks} : Create a new homework.
     *
     * @param lessonId the lesson for which homework is uploading.
     * @param file the attached file.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new homeworkDTO,
     * or with status {@code 400 (Bad Request)} if the homework has already an ID.
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/lesson/{lessonId}/homework")
    public ResponseEntity<HomeworkResponseDTO> uploadHomeworkForLesson(
            @PathVariable final Long lessonId,
            @RequestParam("file") final MultipartFile file,
            @CurrentUser final Student student
    ) throws URISyntaxException {
        log.debug("REST POST request to save homework for lesson with id: {} and student with id: {}",
                lessonId, student.getId());
        HomeworkResponseDTO result = homeworkService.uploadHomeworkForLesson(lessonId, file, student.getId());
        return ResponseEntity.created(new URI("/api/homeworks/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /homeworks} : Updates an existing homework.
     *
     * @param homeworkDTO the homeworkRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated homeworkResponseDTO,
     * or with status {@code 400 (Bad Request)} if the homeworkResponseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the homeworkResponseDTO couldn't be updated.
     */
    @PreAuthorize("@accessValidator.homeworkAccess(#homeworkDTO.lessonId, #homeworkDTO.studentId)")
    @PutMapping("/homeworks")
    public ResponseEntity<HomeworkResponseDTO> updateHomework(@Valid @RequestBody final HomeworkRequestDTO homeworkDTO) {
        log.debug("REST PUT request to update homework : {}", homeworkDTO);
        if (homeworkDTO.getId() == null) {
            throw new SystemException("Invalid id", ErrorCode.BAD_REQUEST);
        }
        HomeworkResponseDTO result = homeworkService.save(homeworkDTO);
        return ResponseEntity.ok()
                .headers(entityHeaderCreator.createEntityUpdateAlert(ENTITY_NAME, homeworkDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /homeworks} : get all the homeworks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of homeworks in body.
     */
    @GetMapping("/homeworks")
    public Page<HomeworkResponseDTO> getAllHomeworks(
            final Filters filters, final Pageable pageable, @CurrentUser final User user) {
        log.debug("REST GET request to get a page of homeworks for user with id: {}", user.getId());
        return homeworkService.findAll(filters, pageable, user);
    }

    /**
     * {@code GET  /homeworks/:id} : get the "id" homework.
     *
     * @param homeworkId the id of the homeworkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the homeworkDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @PostAuthorize("@accessValidator.homeworkAccess(returnObject.body.lessonId, returnObject.body.studentId)")
    @GetMapping("/homeworks/{homeworkId}")
    public ResponseEntity<HomeworkResponseDTO> getHomework(@PathVariable final Long homeworkId) {
        log.debug("REST GET request to get a homework with id: {}", homeworkId);
        Optional<HomeworkResponseDTO> homeworkDTO = homeworkService.findById(homeworkId);
        return ResponseUtil.wrapOrNotFound(homeworkDTO);
    }

    /**
     * {@code DELETE  /homeworks/:id} : delete the "id" homework.
     *
     * @param homeworkId the id of the homeworkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/homeworks/{homeworkId}")
    public ResponseEntity<Void> deleteHomework(@PathVariable final Long homeworkId) {
        log.debug("REST DELETE request to delete a homework with id: {}", homeworkId);
        homeworkService.delete(homeworkId);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, homeworkId.toString()))
                .build();
    }
}
