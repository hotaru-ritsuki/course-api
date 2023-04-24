CREATE TABLE IF NOT EXISTS course_management.course_feedbacks
(
    course_feedback_id SERIAL
        constraint pk_course_feedbacks primary key,
    student_id         INTEGER,
    course_id          INTEGER,
    feedback           TEXT NOT NULL,
    created_date       timestamp   NOT NULL,
    modified_date      timestamp   NOT NULL,
    created_by         VARCHAR(50) NOT NULL,
    modified_by        VARCHAR(50) NOT NULL,
    constraint fk_course_feedbacks_student_id foreign key (student_id) references course_management.users (user_id),
    constraint fk_course_feedbacks_course_id foreign key (course_id) references course_management.courses (course_id)
);

