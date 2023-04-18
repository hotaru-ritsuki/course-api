package com.example.courseapi.service;

import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User saveUser(User user);

    void addRoleToUser(String email, Roles role);

    User findUserByEmail(String email);

    User getUserById(Long id);
}
