package com.example.courseapi.config;

import org.testcontainers.containers.PostgreSQLContainer;

public final class CourseAPIPostgreSQLContainer extends PostgreSQLContainer<CourseAPIPostgreSQLContainer> {
    private static CourseAPIPostgreSQLContainer container;

    private CourseAPIPostgreSQLContainer() {
        super("postgres:14.7-alpine");
    }

    public static CourseAPIPostgreSQLContainer getInstance() {
        if (container == null) {
            container = new CourseAPIPostgreSQLContainer()
                    .withDatabaseName("courseplatform")
                    .withReuse(true);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("SPRING_DATASOURCE_URL", container.getJdbcUrl());
        System.setProperty("SPRING_DATASOURCE_USERNAME", container.getUsername());
        System.setProperty("SPRING_DATASOURCE_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        super.stop();
    }
}
