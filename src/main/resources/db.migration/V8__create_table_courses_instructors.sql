CREATE TABLE IF NOT EXISTS course_management.courses_instructors
(
    instructor_id         INTEGER,
    course_id             INTEGER,
    constraint pk_courses_instructors primary key (instructor_id, course_id),
    constraint fk_courses_instructors_instructor_id foreign key (instructor_id) references course_management.users (user_id),
    constraint fk_courses_instructors_course_id foreign key (course_id) references course_management.courses (course_id)
);

