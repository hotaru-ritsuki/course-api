package com.example.courseapi.util;

import com.example.courseapi.domain.*;
import com.example.courseapi.domain.enums.Roles;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

public final class EntityCreatorUtil {

    private EntityCreatorUtil() {
        throw new IllegalStateException("Can not create instance of utility class.");
    }

    /**
     * Create a User entity for the test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static User createUser(String uuid) {
        return User.builder()
                .firstName("FirstName#" + uuid)
                .lastName("LastName#" + uuid)
                .email("user" + uuid + "@email.com")
                .password("$2a$12$icb9BIES3BgjXkHv1V2acu4YPcYJGNUVwjg2gZtyuSDVO4bQ/Flte")
                .role(Roles.STUDENT)
                .build();
    }

    /**
     * Create a Student entity for the test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Student createStudent(String uuid) {
        return Student.builder()
                .firstName("FirstName#" + uuid)
                .lastName("LastName#" + uuid)
                .email("student" + uuid + "@email.com")
                .password("$2a$12$icb9BIES3BgjXkHv1V2acu4YPcYJGNUVwjg2gZtyuSDVO4bQ/Flte")
                .role(Roles.STUDENT)
                .build();
    }

    public static Student createStudent() {
        return createStudent("");
    }

    /**
     * Create an Instructor entity for the test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Instructor createInstructor(String uuid) {
        return Instructor.builder()
                .firstName("FirstName#" + uuid)
                .lastName("LastName#" + uuid)
                .email("instructor" + uuid + "@email.com")
                .password("$2a$12$icb9BIES3BgjXkHv1V2acu4YPcYJGNUVwjg2gZtyuSDVO4bQ/Flte")
                .role(Roles.INSTRUCTOR)
                .build();
    }

    public static Instructor createInstructor() {
        return createInstructor("");
    }

    /**
     * Create an Admin entity for the test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Admin createAdmin() {
        return Admin.builder()
                .firstName("FirstName#")
                .lastName("LastName#")
                .email("admin@email.com")
                .password("$2a$12$icb9BIES3BgjXkHv1V2acu4YPcYJGNUVwjg2gZtyuSDVO4bQ/Flte")
                .role(Roles.ADMIN)
                .build();
    }

    /**
     * Create a Lesson entity for the test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Lesson createLesson(String uuid, Course course) {
        return Lesson.builder()
                .title("Title#" + uuid)
                .description("Description#" + uuid)
                .course(course)
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    /**
     * Create a Course entity for the test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Course createCourse(String uuid, Set<Instructor> instructor) {
        return Course.builder()
                .description("Course description #" + uuid)
                .title("Course title #" +uuid)
                .instructors(instructor)
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    /**
     * Create an CourseFeedback entity for the test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static CourseFeedback createCourseFeedback(String uuid, Student student, Course course) {
        return CourseFeedback.builder()
                .feedback("Feedback#" + uuid)
                .student(student)
                .course(course)
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    /**
     * Create a Homework entity for the test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Homework createHomework(String uuid, Lesson lesson, Student student) {
        return Homework.builder()
                .title("Title#" + uuid)
                .filePath("Filepath#" + uuid)
                .lesson(lesson)
                .student(student)
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    /**
     * Create a Homework entity for the test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Submission createSubmission(String uuid, Lesson lesson, Student student, Double lowerBound) {
        return Submission.builder()
                .grade(new Random().nextDouble(0.0, 100.0))
                .lesson(lesson)
                .student(student)
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    /**
     * Create a Homework entity for the test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Submission createSubmission(String uuid, Lesson lesson, Student student) {
        return createSubmission(uuid, lesson, student, 0.0);
    }
}
