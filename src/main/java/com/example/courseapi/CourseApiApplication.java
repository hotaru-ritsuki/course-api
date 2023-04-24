package com.example.courseapi;

import com.example.courseapi.repository.CourseRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.service.mapper.CourseStatusMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CourseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseApiApplication.class, args);
    }

}
