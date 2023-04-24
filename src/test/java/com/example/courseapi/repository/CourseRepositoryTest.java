package com.example.courseapi.repository;


import com.example.courseapi.config.DefaultRepositoryTestConfiguration;
import com.example.courseapi.domain.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@DefaultRepositoryTestConfiguration
public class CourseRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void should_find_no_courses_if_repository_is_empty() {
        List<Course> courses = courseRepository.findAll();

        assertThat(courses).isEmpty();
    }

    @Test
    public void should_store_a_course() {
        Course course = courseRepository.save(
                Course.builder()
                        .description("Course description")
                        .title("Course title")
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        assertThat(course).hasFieldOrPropertyWithValue("title", "Course title");
        assertThat(course).hasFieldOrPropertyWithValue("description", "Course description");
    }

    @Test
    public void should_find_all_courses() {
        Course course1 = Course.builder()
                .description("DESCRIPTION#1")
                .title("TITLE#1")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course1);

        Course course2 = Course.builder()
                .description("DESCRIPTION#2")
                .title("TITLE#2")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course2);

        Course course3 = Course.builder()
                .description("DESCRIPTION#3")
                .title("TITLE#3")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course3);

        List<Course> courses = courseRepository.findAll();

        assertThat(courses).hasSize(3).contains(course1, course2, course3);
    }

    @Test
    public void should_find_course_by_id() {
        Course course1 = Course.builder()
                .description("DESCRIPTION#1")
                .title("TITLE#1")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course1);

        Course course2 = Course.builder()
                .description("DESCRIPTION#2")
                .title("TITLE#2")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course2);


        Optional<Course> foundCourseOpt = courseRepository.findById(course2.getId());
        assertThat(foundCourseOpt).isPresent();

        Course foundCourse = foundCourseOpt.get();
        assertThat(foundCourse).isEqualTo(course2);
    }

    @Test
    public void should_find_courses_by_title_containing_string() {
        Course course1 = Course.builder()
                .description("DESCRIPTION#1")
                .title("TITLE#1")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course1);

        Course course2 = Course.builder()
                .description("DESCRIPTION#2")
                .title("TITLE Spring#2")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course2);

        Course course3 = Course.builder()
                .description("DESCRIPTION#3")
                .title("TITLE Spring#3")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course3);

        List<Course> courses = courseRepository.findByTitleContaining("ring");

        assertThat(courses).hasSize(2).contains(course2, course3);
    }

    @Test
    public void should_find_courses_by_description_containing_string() {
        Course course1 = Course.builder()
                .description("DESCRIPTION test#1")
                .title("TITLE#1")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course1);

        Course course2 = Course.builder()
                .description("DESCRIPTION test#2")
                .title("TITLE#2")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course2);

        Course course3 = Course.builder()
                .description("DESCRIPTION#3")
                .title("TITLE#3")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course3);

        List<Course> courses = courseRepository.findByDescriptionContaining("test");

        assertThat(courses).hasSize(2).contains(course1, course2);
    }

    @Test
    public void should_update_course_by_id() {
        Course course1 = Course.builder()
                .description("DESCRIPTION#1")
                .title("TITLE#1")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course1);

        Course course2 = Course.builder()
                .description("DESCRIPTION#2")
                .title("TITLE#2")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course2);

        Optional<Course> courseOpt = courseRepository.findById(course2.getId());
        assertThat(courseOpt).isPresent();

        Course course = courseOpt.get();
        course.setTitle("TITLE Spring#2");
        course.setDescription("DESCRIPTION#2");
        courseRepository.save(course);

        Optional<Course> checkCourseOpt = courseRepository.findById(course2.getId());
        assertThat(checkCourseOpt).isPresent();

        Course checkCourse = checkCourseOpt.get();

        assertThat(checkCourse.getId()).isEqualTo(course2.getId());
        assertThat(checkCourse.getTitle()).isEqualTo(course.getTitle());
        assertThat(checkCourse.getDescription()).isEqualTo(course.getDescription());
    }

    @Test
    public void should_delete_course_by_id() {
        Course course1 = Course.builder()
                .description("DESCRIPTION#1")
                .title("TITLE#1")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course1);

        Course course2 = Course.builder()
                .description("DESCRIPTION#2")
                .title("TITLE#2")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course2);

        Course course3 = Course.builder()
                .description("DESCRIPTION#3")
                .title("TITLE#3")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course3);

        courseRepository.deleteById(course2.getId());

        List<Course> courses = courseRepository.findAll();

        assertThat(courses).hasSize(2).contains(course1, course3);
    }

    @Test
    public void should_delete_all_courses() {
        Course course1 = Course.builder()
                .description("DESCRIPTION#1")
                .title("TITLE#1")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course1);

        Course course2 = Course.builder()
                .description("DESCRIPTION#2")
                .title("TITLE#2")
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
        entityManager.persist(course2);

        courseRepository.deleteAll();

        assertThat(courseRepository.count()).isEqualTo(0L);
    }
}
