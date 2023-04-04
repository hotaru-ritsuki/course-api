CREATE TABLE IF NOT EXISTS course_management.lessons
(
    lesson_id     SERIAL constraint pk_lessons primary key,
    title         VARCHAR(100) NOT NULL,
    description   VARCHAR(255),
    course_id     INTEGER,
    created_date  timestamp    NOT NULL,
    modified_date timestamp    NOT NULL,
    created_by    VARCHAR(50)  NOT NULL,
    modified_by   VARCHAR(50)  NOT NULL,
    constraint fk_lessons_course_id foreign key (course_id) references course_management.courses (course_id)
);