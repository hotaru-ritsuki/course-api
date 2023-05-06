package com.example.courseapi.config;

import org.testcontainers.containers.PostgreSQLContainer;

public abstract class PostgresRepositoryTestContainer {

    static final PostgreSQLContainer<CourseAPIRepositoryPostgreSQLContainer> postgreSQLContainer;

    static {
        postgreSQLContainer = CourseAPIRepositoryPostgreSQLContainer.getInstance();
        postgreSQLContainer.start();
    }

}