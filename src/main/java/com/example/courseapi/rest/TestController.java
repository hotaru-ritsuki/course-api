package com.example.courseapi.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {


    @GetMapping("/testing")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Tests");
    }
}
