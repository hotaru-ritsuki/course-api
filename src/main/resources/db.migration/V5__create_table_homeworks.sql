CREATE TABLE IF NOT EXISTS course_management.homeworks
(
    id   SERIAL constraint pk_homeworks primary key,
    student_id    INTEGER,
    lesson_id     INTEGER,
    title         VARCHAR(50)  NOT NULL,
    file_path     VARCHAR(255) NOT NULL,
    created_date  timestamp    NOT NULL,
    modified_date timestamp    NOT NULL,
    created_by    VARCHAR(50)  NOT NULL,
    modified_by   VARCHAR(50)  NOT NULL,
    constraint fk_homeworks_student_id foreign key (student_id) references course_management.users (id),
    constraint fk_homeworks_lesson_id foreign key (lesson_id) references course_management.lessons (id)
);
