package com.example.courseapi.service.impl;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.specs.SpecificationBuilder;
import com.example.courseapi.domain.Instructor;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.UserRequestDTO;
import com.example.courseapi.dto.UserResponseDTO;
import com.example.courseapi.exception.UserNotFoundException;
import com.example.courseapi.repository.AdminRepository;
import com.example.courseapi.repository.InstructorRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.UserService;
import com.example.courseapi.service.mapper.InstructorMapper;
import com.example.courseapi.service.mapper.StudentMapper;
import com.example.courseapi.service.mapper.UserMapper;
import com.example.courseapi.service.mapper.UserRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRequestMapper userRequestMapper;
    private final StudentMapper studentMapper;
    private final InstructorMapper instructorMapper;

    @Override
    public Page<UserResponseDTO> getUsers(Filters filters, Pageable pageable) {
        return userRepository.findAll(new SpecificationBuilder<User>(filters).build(), pageable)
                .map(userMapper::toDto);

    }

    @Override
    public UserResponseDTO saveUser(UserRequestDTO userDTO) {
        User user = userRequestMapper.toEntity(userDTO);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateUser(UserRequestDTO userDTO) {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(UserNotFoundException::new);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void addRoleToUser(String email, Roles role) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(role);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public UserResponseDTO findUserByEmail(String email) {
        return userMapper.toDto(userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserResponseDTO mapCurrentUser(User user) {
        if (user instanceof Student currentStudent) {
            return studentMapper.toDto(currentStudent);
        } else if (user instanceof Instructor currentInstructor) {
            return instructorMapper.toDto(currentInstructor);
        } else {
            return userMapper.toDto(user);
        }
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponseDTO assingRoleForUser(Long userId, Roles role) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setRole(role);
        user = userRepository.saveAndFlush(user);
        return mapCurrentUser(user);
    }
}
