package com.example.courseapi;

import com.example.courseapi.config.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CourseApiApplicationTests extends PostgresTestContainer {

    @Test
    void contextLoads() {
    }

}
