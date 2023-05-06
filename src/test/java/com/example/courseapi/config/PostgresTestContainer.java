package com.example.courseapi.config;

import org.testcontainers.containers.PostgreSQLContainer;

public abstract class PostgresTestContainer {

    static final PostgreSQLContainer<CourseAPIPostgreSQLContainer> postgreSQLContainer;

    static {
        postgreSQLContainer = CourseAPIPostgreSQLContainer.getInstance();
        postgreSQLContainer.start();
    }

}
