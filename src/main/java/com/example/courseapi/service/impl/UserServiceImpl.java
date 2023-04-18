package com.example.courseapi.service.impl;

import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.exception.UserNotFoundException;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
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
    public User findUserByEmail(String email) {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
