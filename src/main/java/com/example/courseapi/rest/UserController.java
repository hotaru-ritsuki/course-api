package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.domain.User;
import com.example.courseapi.dto.CourseDTO;
import com.example.courseapi.dto.RoleDTO;
import com.example.courseapi.dto.UserRequestDTO;
import com.example.courseapi.dto.UserResponseDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.security.annotation.CurrentUser;
import com.example.courseapi.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final String ENTITY_NAME = "User";

    private final UserService userService;
    private final EntityHeaderCreator entityHeaderCreator;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDTO>> getUsers(Filters filters, Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(filters, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> saveUser(@Valid @RequestBody UserRequestDTO userDTO) throws URISyntaxException {
        if (userDTO.getId() != null) {
            throw new SystemException("A new user cannot already have an ID", ErrorCode.BAD_REQUEST);
        }
        UserResponseDTO result = userService.saveUser(userDTO);
        return ResponseEntity.created(new URI("/api/users/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/{userId}/role")
    public ResponseEntity<UserResponseDTO> assignRoleForUser(@PathVariable Long userId, @Valid @RequestBody RoleDTO roleDTO)
            throws URISyntaxException {
        UserResponseDTO result = userService.assingRoleForUser(userId, roleDTO.getRole());
        return ResponseEntity.created(new URI("/api/users/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserRequestDTO userDTO) throws URISyntaxException {
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
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent()
                .headers(entityHeaderCreator.createEntityDeletionAlert(ENTITY_NAME, userId.toString()))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'INSTRUCTOR')")
    @GetMapping("/users/me")
    public ResponseEntity<? extends UserResponseDTO> me(@NotNull @CurrentUser User currentUser) {
        return ResponseEntity.ok(userService.mapCurrentUser(currentUser));
    }
}

