package com.example.courseapi.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class CourseAPIRepositoryPostgreSQLContainer extends PostgreSQLContainer<CourseAPIRepositoryPostgreSQLContainer> {
    private static CourseAPIRepositoryPostgreSQLContainer container;

    private CourseAPIRepositoryPostgreSQLContainer() {
        super("postgres:14.7-alpine");
    }

    public static CourseAPIRepositoryPostgreSQLContainer getInstance() {
        if (container == null) {
            container = new CourseAPIRepositoryPostgreSQLContainer()
                    .withDatabaseName("courseplatform")
                    .withReuse(true);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("SPRING_REPOSITORY_DATASOURCE_URL", container.getJdbcUrl());
        System.setProperty("SPRING_REPOSITORY_DATASOURCE_USERNAME", container.getUsername());
        System.setProperty("SPRING_REPOSITORY_DATASOURCE_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        super.stop();
    }
}