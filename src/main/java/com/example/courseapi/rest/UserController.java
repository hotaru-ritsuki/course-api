package com.example.courseapi.rest;

import com.example.courseapi.config.EntityHeaderCreator;
import com.example.courseapi.domain.User;
import com.example.courseapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final String ENTITY_NAME = "User";

    private final UserService userService;
    private final EntityHeaderCreator entityHeaderCreator;

    @GetMapping("/users")
    public ResponseEntity<Collection<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) throws URISyntaxException {
        User result = userService.saveUser(user);
        return ResponseEntity.created(new URI("/api/users/" + result.getId()))
                .headers(entityHeaderCreator.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> saveUser(@RequestParam Long id) throws URISyntaxException {
        return ResponseEntity.ok(userService.getUserById(id));
    }


}

