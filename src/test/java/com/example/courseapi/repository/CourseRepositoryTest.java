package com.example.courseapi.repository;


import com.example.courseapi.config.DefaultJPARepositoryTestConfiguration;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Instructor;
import com.example.courseapi.util.EntityCreatorUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

@SuppressWarnings("unused")
@DefaultJPARepositoryTestConfiguration
public class CourseRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    private Set<Instructor> instructor;
    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
        Set<Instructor> instructorSet = new HashSet<>();
        instructorSet.add(instructorRepository.save(EntityCreatorUtil.createInstructor()));
        this.instructor = instructorSet;
    }

    @Test
    public void should_find_no_courses_if_repository_is_empty() {
        List<Course> courses = courseRepository.findAll();

        assertThat(courses).isEmpty();
    }

    @Test
    public void should_store_a_course() {
        Course course = courseRepository.save(EntityCreatorUtil.createCourse("", instructor));

        assertThat(course).hasFieldOrPropertyWithValue("title", "Course title #");
        assertThat(course).hasFieldOrPropertyWithValue("description", "Course description #");
    }

    @Test
    public void should_find_all_courses() {
        Course course1 = EntityCreatorUtil.createCourse("1", instructor);
        entityManager.persist(course1);

        Course course2 = EntityCreatorUtil.createCourse("2", instructor);
        entityManager.persist(course2);

        Course course3 = EntityCreatorUtil.createCourse("3", instructor);
        entityManager.persist(course3);

        List<Course> courses = courseRepository.findAll();

        assertThat(courses).hasSize(3).contains(course1, course2, course3);
    }

    @Test
    public void should_find_course_by_id() {
        Course course1 = EntityCreatorUtil.createCourse("1", instructor);
        entityManager.persist(course1);

        Course course2 = EntityCreatorUtil.createCourse("2", instructor);
        entityManager.persist(course2);


        Optional<Course> foundCourseOpt = courseRepository.findById(course2.getId());
        assertThat(foundCourseOpt).isPresent();

        Course foundCourse = foundCourseOpt.get();
        assertThat(foundCourse).isEqualTo(course2);
    }

    @Test
    public void should_find_courses_by_title_containing_string() {
        Course course1 = EntityCreatorUtil.createCourse("1", instructor);
        entityManager.persist(course1);

        Course course2 = EntityCreatorUtil.createCourse("2", instructor);
        course2.setTitle("Titlering");
        entityManager.persist(course2);

        Course course3 = EntityCreatorUtil.createCourse("3", instructor);
        course3.setTitle("Ringring");
        entityManager.persist(course3);

        List<Course> courses = courseRepository.findByTitleContaining("ring");

        assertThat(courses).hasSize(2).contains(course2, course3);
    }

    @Test
    public void should_find_courses_by_description_containing_string() {
        Course course1 = EntityCreatorUtil.createCourse("1", instructor);
        course1.setDescription("DESCtestDESCDESC");
        entityManager.persist(course1);

        Course course2 = EntityCreatorUtil.createCourse("2", instructor);
        course2.setDescription("testDESCDESCDESC");
        entityManager.persist(course2);

        Course course3 = EntityCreatorUtil.createCourse("3", instructor);
        entityManager.persist(course3);

        List<Course> courses = courseRepository.findByDescriptionContaining("test");

        assertThat(courses).hasSize(2).contains(course1, course2);
    }

    @Test
    public void should_update_course_by_id() {
        Course course1 = EntityCreatorUtil.createCourse("1", instructor);
        entityManager.persist(course1);

        Course course2 = EntityCreatorUtil.createCourse("2", instructor);
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
        Course course1 = EntityCreatorUtil.createCourse("1", instructor);
        entityManager.persist(course1);

        Course course2 = EntityCreatorUtil.createCourse("2", instructor);
        entityManager.persist(course2);

        Course course3 = EntityCreatorUtil.createCourse("3", instructor);
        entityManager.persist(course3);

        courseRepository.deleteById(course2.getId());

        List<Course> courses = courseRepository.findAll();

        assertThat(courses).hasSize(2).contains(course1, course3);
    }

    @Test
    public void should_delete_all_courses() {
        Course course1 = EntityCreatorUtil.createCourse("1", instructor);
        entityManager.persist(course1);

        Course course2 = EntityCreatorUtil.createCourse("2", instructor);
        entityManager.persist(course2);

        courseRepository.deleteAll();

        assertThat(courseRepository.count()).isEqualTo(0L);
    }
}
