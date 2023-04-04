CREATE TABLE IF NOT EXISTS course_management.courses_students
(
    student_id         INTEGER,
    course_id          INTEGER,
    constraint pk_courses_students primary key (student_id, course_id),
    constraint fk_homeworks_student_id foreign key (student_id) references course_management.users (user_id),
    constraint fk_homeworks_lesson_id foreign key (course_id) references course_management.courses (course_id)
);

