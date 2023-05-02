package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.domain.User;
import com.example.courseapi.dto.request.RoleRequestDTO;
import com.example.courseapi.dto.response.UserResponseDTO;
import com.example.courseapi.dto.request.UserRequestDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.security.annotation.CurrentUser;
import com.example.courseapi.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Log4j2
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final String ENTITY_NAME = "User";

    private final UserService userService;
    private final EntityHeaderCreator entityHeaderCreator;

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDTO>> getUsers(final Filters filters, final Pageable pageable) {
        log.debug("REST GET request to get a page of users");
        return ResponseEntity.ok(userService.getUsers(filters, pageable));
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> saveUser(@Valid @RequestBody final UserRequestDTO userDTO)
            throws URISyntaxException {
        log.debug("REST POST request to save user : {}", userDTO);
        if (userDTO.getId() != null) {
            throw new SystemException("A new user cannot already have an ID", ErrorCode.BAD_REQUEST);
        }
        UserResponseDTO result = userService.saveUser(userDTO);
        return ResponseEntity.created(new URI("/api/users/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @RequestMapping(value = "/users/{userId}/role", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<UserResponseDTO> assignRoleForUser(
            @PathVariable final Long userId, @Valid @RequestBody final RoleRequestDTO roleDTO) {
        log.debug("REST POST request to assign role:{} user with id : {}", roleDTO, userId);
        UserResponseDTO result = userService.assingRoleForUser(userId, roleDTO.getRole());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/users")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody final UserRequestDTO userDTO) {
        log.debug("REST POST request to update user : {}", userDTO);
        if (userDTO.getId() == null) {
            throw new SystemException("Invalid user id", ErrorCode.BAD_REQUEST);
        }
        UserResponseDTO result = userService.updateUser(userDTO);
        return ResponseEntity.ok()
                .headers(entityHeaderCreator.createEntityUpdateAlert(ENTITY_NAME, userDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code DELETE  /users/:id} : delete the "id" user.
     *
     * @param userId the id of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable final Long userId) {
        log.debug("REST DELETE request to delete user with id: {}", userId);
        userService.delete(userId);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, userId.toString()))
                .build();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable final Long userId) {
        log.debug("REST GET request to get a user with id: {}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'INSTRUCTOR')")
    @GetMapping("/users/me")
    public ResponseEntity<? extends UserResponseDTO> me(@NotNull @CurrentUser final User currentUser) {
        log.debug("REST GET request to get current user authentication");
        return ResponseEntity.ok(userService.mapCurrentUser(currentUser));
    }
}

