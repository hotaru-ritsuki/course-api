package com.example.courseapi.repository;


import com.example.courseapi.config.DefaultRepositoryTestConfiguration;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.enums.Roles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
@DefaultRepositoryTestConfiguration
public class CourseFeedbackRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseFeedbackRepository courseFeedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    private static Student createStudent(String uuid) {
        return Student.builder()
                .firstName("FirstName#" + uuid)
                .lastName("LastName#" + uuid)
                .email("student" + uuid + "@email.com")
                .password("TestPassword")
                .role(Roles.STUDENT)
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    private static Course createCourse(String uuid) {
        return Course.builder()
                .description("Course description #" + uuid)
                .title("Course title #" +uuid)
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    @Test
    public void should_find_no_course_feedbacks_if_repository_is_empty() {
        List<CourseFeedback> courseFeedbacks = courseFeedbackRepository.findAll();

        assertThat(courseFeedbacks).isEmpty();
    }

    @Test
    public void should_store_a_course_feedback() {
        Student student = userRepository.save(createStudent("1"));
        Course course = courseRepository.save(createCourse("1"));

        CourseFeedback courseFeedback = courseFeedbackRepository.save(
                CourseFeedback.builder()
                        .feedback("CourseFeedback title")
                        .student(student)
                        .course(course)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        assertThat(courseFeedback).hasFieldOrPropertyWithValue("feedback", "CourseFeedback title");
        assertThat(courseFeedback).hasFieldOrPropertyWithValue("student", student);
        assertThat(courseFeedback).hasFieldOrPropertyWithValue("course", course);
    }

    @Test
    public void should_find_all_course_feedbacks() {
        Student student1 = entityManager.persist(createStudent("1"));
        Course course1 = entityManager.persist(createCourse("1"));

        CourseFeedback courseFeedback1 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#1")
                        .student(student1)
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Student student2 = entityManager.persist(createStudent("2"));
        Course course2 = entityManager.persist(createCourse("2"));

        CourseFeedback courseFeedback2 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#2")
                        .student(student2)
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Student student3 = entityManager.persist(createStudent("3"));
        Course course3 = entityManager.persist(createCourse("3"));

        CourseFeedback courseFeedback3 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#3")
                        .student(student3)
                        .course(course3)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        List<CourseFeedback> courseFeedbacks = courseFeedbackRepository.findAll();

        assertThat(courseFeedbacks).hasSize(3)
                .contains(courseFeedback1, courseFeedback2, courseFeedback3);
    }

    @Test
    public void should_find_course_feedback_by_id() {
        Student student1 = entityManager.persist(createStudent("1"));
        Course course1 = entityManager.persist(createCourse("1"));

        CourseFeedback courseFeedback1 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#1")
                        .student(student1)
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Student student2 = entityManager.persist(createStudent("2"));
        Course course2 = entityManager.persist(createCourse("2"));

        CourseFeedback courseFeedback2 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#2")
                        .student(student2)
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Optional<CourseFeedback> foundCourseOpt = courseFeedbackRepository.findById(courseFeedback2.getId());
        assertThat(foundCourseOpt).isPresent();

        CourseFeedback foundCoursefeedback = foundCourseOpt.get();
        assertThat(foundCoursefeedback).isEqualTo(courseFeedback2);
    }

    @Test
    public void should_find_course_feedbacks_by_feedback_containing_string() {
        Student student1 = entityManager.persist(createStudent("1"));
        Course course1 = entityManager.persist(createCourse("1"));

        CourseFeedback courseFeedback1 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#1")
                        .student(student1)
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Student student2 = entityManager.persist(createStudent("2"));
        Course course2 = entityManager.persist(createCourse("2"));

        CourseFeedback courseFeedback2 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#2 test")
                        .student(student2)
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Student student3 = entityManager.persist(createStudent("3"));
        Course course3 = entityManager.persist(createCourse("3"));

        CourseFeedback courseFeedback3 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#3 test LOL")
                        .student(student3)
                        .course(course3)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        List<CourseFeedback> courseFeedbacks = courseFeedbackRepository.findByFeedbackContaining("test");

        assertThat(courseFeedbacks).hasSize(2).contains(courseFeedback2, courseFeedback3);
    }

    @Test
    public void should_update_course_feedback_by_id() {
        Student student1 = entityManager.persist(createStudent("1"));
        Course course1 = entityManager.persist(createCourse("1"));

        CourseFeedback courseFeedback1 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#1")
                        .student(student1)
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Student student2 = entityManager.persist(createStudent("2"));
        Course course2 = entityManager.persist(createCourse("2"));

        CourseFeedback courseFeedback2 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#2 test")
                        .student(student2)
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Optional<CourseFeedback> courseOpt = courseFeedbackRepository.findById(courseFeedback2.getId());
        assertThat(courseOpt).isPresent();

        CourseFeedback courseFeedback = courseOpt.get();
        courseFeedback.setFeedback("UPDATED FEEDBACK");
        courseFeedbackRepository.save(courseFeedback);

        Optional<CourseFeedback> checkCourseOpt = courseFeedbackRepository.findById(courseFeedback2.getId());
        assertThat(checkCourseOpt).isPresent();

        CourseFeedback checkCourse = checkCourseOpt.get();

        assertThat(checkCourse.getId()).isEqualTo(courseFeedback.getId());
        assertThat(checkCourse.getFeedback()).isEqualTo(courseFeedback.getFeedback());
    }

    @Test
    public void should_delete_course_feedback_by_id() {
        Student student1 = entityManager.persist(createStudent("1"));
        Course course1 = entityManager.persist(createCourse("1"));

        CourseFeedback courseFeedback1 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#1")
                        .student(student1)
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Student student2 = entityManager.persist(createStudent("2"));
        Course course2 = entityManager.persist(createCourse("2"));

        CourseFeedback courseFeedback2 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#2 test")
                        .student(student2)
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Student student3 = entityManager.persist(createStudent("3"));
        Course course3 = entityManager.persist(createCourse("3"));

        CourseFeedback courseFeedback3 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#3 test LOL")
                        .student(student3)
                        .course(course3)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        courseFeedbackRepository.deleteById(courseFeedback2.getId());

        List<CourseFeedback> courseFeedbacks = courseFeedbackRepository.findAll();

        assertThat(courseFeedbacks).hasSize(2).contains(courseFeedback1, courseFeedback3);
    }

    @Test
    public void should_delete_all_course_feedbacks() {
        Student student1 = entityManager.persist(createStudent("1"));
        Course course1 = entityManager.persist(createCourse("1"));

        CourseFeedback courseFeedback1 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#1")
                        .student(student1)
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Student student2 = entityManager.persist(createStudent("2"));
        Course course2 = entityManager.persist(createCourse("2"));

        CourseFeedback courseFeedback2 = entityManager.persist(
                CourseFeedback.builder()
                        .feedback("Feedback#2 test")
                        .student(student2)
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        courseFeedbackRepository.deleteAll();

        assertThat(courseFeedbackRepository.count()).isEqualTo(0L);
    }
}
