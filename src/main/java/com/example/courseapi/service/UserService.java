package com.example.courseapi.service;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.response.UserResponseDTO;
import com.example.courseapi.dto.request.UserRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link User} entity.
 */
public interface UserService {

    /**
     * Get all users based on filters and pageable information.
     *
     * @param filters  the optional filters to apply when searching for courses
     * @param pageable the pagination information to apply when retrieving the courses
     * @return a page of user response DTOs
     */
    Page<UserResponseDTO> getUsers(final Filters filters, final Pageable pageable);

    /**
     * Create a new user.
     *
     * @param userDTO the user request DTO to create a new user
     * @return the user response DTO of the newly created user
     */
    UserResponseDTO saveUser(final UserRequestDTO userDTO);

    /**
     * Update an existing user.
     *
     * @param userDTO the user request DTO to update an existing user
     * @return the user response DTO of the updated user
     */
    UserResponseDTO updateUser(final UserRequestDTO userDTO);

    /**
     * Add a role to an existing user.
     *
     * @param email the email of the user to add the role to
     * @param role  the role to add to the user
     */
    void addRoleToUser(final String email, final Roles role);

    /**
     * Find a user by email.
     *
     * @param email the email of the user to find
     * @return the user response DTO of the found user
     */
    UserResponseDTO findUserByEmail(final String email);

    /**
     * Find a user by ID.
     *
     * @param id the ID of the user to find
     * @return the user response DTO of the found user
     */
    UserResponseDTO getUserById(final Long id);

    /**
     * Map a current user to a user response DTO.
     *
     * @param currentUser the current user to map
     * @return the user response DTO of the current user
     */
    UserResponseDTO mapCurrentUser(final User currentUser);

    /**
     * Delete a user by ID.
     *
     * @param userId the ID of the user to delete
     */
    void delete(Long userId);

    UserResponseDTO assingRoleForUser(final Long userId, final Roles role);
}