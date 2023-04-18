package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.controller.util.ResponseUtil;
import com.example.courseapi.dto.HomeworkDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.service.HomeworkService;
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
 * REST controller for managing {@link com.example.courseapi.domain.Homework}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class HomeworkController {
    private static final String ENTITY_NAME = "Homework";

    private final HomeworkService homeworkService;
    private final EntityHeaderCreator entityHeaderCreator;

    /**
     * {@code POST /homeworks} : Create a new homework.
     *
     * @param homeworkDTO the homework to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new homeworkDTO,
     * or with status {@code 400 (Bad Request)} if the homework has already an ID.
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/homeworks")
    public ResponseEntity<HomeworkDTO> createHomework(@Valid @RequestBody HomeworkDTO homeworkDTO) throws URISyntaxException {
        if (homeworkDTO.getId() != null) {
            throw new SystemException("A new homework cannot already have an ID", ErrorCode.BAD_REQUEST);
        }
        HomeworkDTO result = homeworkService.save(homeworkDTO);
        return ResponseEntity.created(new URI("/api/homeworks/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /homeworks} : Updates an existing homework.
     *
     * @param homeworkDTO the homeworkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated homeworkDTO,
     * or with status {@code 400 (Bad Request)} if the homeworkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the homeworkDTO couldn't be updated.
     */
    @PutMapping("/homeworks")
    public ResponseEntity<HomeworkDTO> updateHomework(@Valid @RequestBody HomeworkDTO homeworkDTO) {
        if (homeworkDTO.getId() == null) {
            throw new SystemException("Invalid id", ErrorCode.BAD_REQUEST);
        }
        HomeworkDTO result = homeworkService.save(homeworkDTO);
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
    public List<HomeworkDTO> getAllHomeworks() {
        return homeworkService.findAll();
    }

    /**
     * {@code GET  /homeworks/:id} : get the "id" homework.
     *
     * @param id the id of the homeworkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the homeworkDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/homeworks/{id}")
    public ResponseEntity<HomeworkDTO> getHomework(@PathVariable Long id) {
        Optional<HomeworkDTO> homeworkDTO = homeworkService.findById(id);
        return ResponseUtil.wrapOrNotFound(homeworkDTO);
    }

    /**
     * {@code DELETE  /homeworks/:id} : delete the "id" homework.
     *
     * @param id the id of the homeworkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/homeworks/{id}")
    public ResponseEntity<Void> deleteHomework(@PathVariable Long id) {
        homeworkService.delete(id);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
                .build();
    }
}
