CREATE TABLE IF NOT EXISTS course_management.courses
(
    id     SERIAL constraint pk_courses primary key,
    title         VARCHAR(100) NOT NULL,
    description   VARCHAR(255),
    created_date  timestamp    NOT NULL,
    modified_date timestamp    NOT NULL,
    created_by    VARCHAR(50)  NOT NULL,
    modified_by   VARCHAR(50)  NOT NULL
);