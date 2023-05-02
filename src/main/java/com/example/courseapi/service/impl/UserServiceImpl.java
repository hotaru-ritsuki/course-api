package com.example.courseapi.service.impl;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.specs.SpecificationBuilder;
import com.example.courseapi.domain.Instructor;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.response.UserResponseDTO;
import com.example.courseapi.dto.request.UserRequestDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.UserService;
import com.example.courseapi.service.mapper.InstructorMapper;
import com.example.courseapi.service.mapper.StudentMapper;
import com.example.courseapi.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final StudentMapper studentMapper;
    private final InstructorMapper instructorMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getUsers(final Filters filters, final Pageable pageable) {
        log.debug("Finding all users by filters and pageable");
        return userRepository.findAll(new SpecificationBuilder<User>(filters).build(), pageable)
                .map(userMapper::toResponseDto);

    }

    @Override
    @Transactional
    public UserResponseDTO saveUser(final UserRequestDTO userDTO) {
        log.debug("Saving user : {}", userDTO);
        User user = userMapper.fromRequestDto(userDTO);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(final UserRequestDTO userDTO) {
        log.debug("Updating user : {}", userDTO);
        User user = userRepository.findById(userDTO.getId()).orElseThrow(() ->
                new SystemException("User with id: " + userDTO.getId() + " not found.", ErrorCode.BAD_REQUEST));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void addRoleToUser(final String email, final Roles role) {
        log.debug("Adding role `{}` to user with email: {}", role, email);
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new SystemException("User with email: " + email + " not found.", ErrorCode.BAD_REQUEST));
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findUserByEmail(final String email) {
        log.debug("Finding user with email: {}", email);
        return userMapper.toResponseDto(userRepository.findByEmail(email).orElseThrow(() ->
                new SystemException("User with email: " + email + " not found.", ErrorCode.NOT_FOUND)));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(final Long userId) {
        log.debug("Finding user with id: {}", userId);
        return userMapper.toResponseDto(userRepository.findById(userId).orElseThrow(() ->
                new SystemException("User with id: " + userId + " not found.", ErrorCode.NOT_FOUND)));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO mapCurrentUser(final User user) {
        log.debug("Mapping current user: {}", user.getId());
        if (user instanceof Student currentStudent) {
            return studentMapper.toResponseDto(currentStudent);
        } else if (user instanceof Instructor currentInstructor) {
            return instructorMapper.toResponseDto(currentInstructor);
        } else {
            return userMapper.toResponseDto(user);
        }
    }

    @Override
    @Transactional
    public void delete(final Long userId) {
        log.debug("Deleting user with id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new SystemException("User with id: " + userId + " not found.", ErrorCode.BAD_REQUEST));
        if (user instanceof Student student) {
            userRepository.delete(student);
        } else if (user instanceof Instructor instructor) {
            userRepository.delete(instructor);
        } else {
            userRepository.deleteById(userId);
        }

    }

    @Override
    @Transactional
    public UserResponseDTO assingRoleForUser(final Long userId, final Roles role) {
        log.debug("Adding role `{}` to user with id: {}", role, userId);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new SystemException("User with id: " + userId + " not found.", ErrorCode.BAD_REQUEST));
        user.setRole(role);
        user = userRepository.saveAndFlush(user);
        return mapCurrentUser(user);
    }
}
