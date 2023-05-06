package com.example.courseapi.repository;


import com.example.courseapi.config.PostgresRepositoryTestContainer;
import com.example.courseapi.config.annotation.DefaultJPARepositoryTestConfiguration;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.domain.Instructor;
import com.example.courseapi.domain.Student;
import com.example.courseapi.util.EntityCreatorUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
@DefaultJPARepositoryTestConfiguration
public class CourseFeedbackRepositoryTest extends PostgresRepositoryTestContainer {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseFeedbackRepository courseFeedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Set<Instructor> instructors;
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
        this.instructors = instructorSet;
    }

    @Transactional
    @Test
    public void should_find_no_course_feedbacks_if_repository_is_empty() {
        List<CourseFeedback> courseFeedbacks = courseFeedbackRepository.findAll();

        assertThat(courseFeedbacks).isEmpty();
    }

    @Transactional
    @Test
    public void should_store_a_course_feedback() {
        Student student = userRepository.save(EntityCreatorUtil.createStudent("1"));
        Course course = courseRepository.save(EntityCreatorUtil.createCourse("1", instructors));

        CourseFeedback courseFeedback = courseFeedbackRepository.save(
                EntityCreatorUtil.createCourseFeedback("1", student, course)
        );

        assertThat(courseFeedback).hasFieldOrPropertyWithValue("feedback", "Feedback#1");
        assertThat(courseFeedback).hasFieldOrPropertyWithValue("student", student);
        assertThat(courseFeedback).hasFieldOrPropertyWithValue("course", course);
    }

    @Transactional
    @Test
    public void should_find_all_course_feedbacks() {
        Student student1 = entityManager.persist(EntityCreatorUtil.createStudent("1"));
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructors));

        CourseFeedback courseFeedback1 = entityManager.persist(
                EntityCreatorUtil.createCourseFeedback("1", student1, course1)
        );

        Student student2 = entityManager.persist(EntityCreatorUtil.createStudent("2"));
        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructors));

        CourseFeedback courseFeedback2 = entityManager.persist(
                EntityCreatorUtil.createCourseFeedback("2", student2, course2)
        );

        Student student3 = entityManager.persist(EntityCreatorUtil.createStudent("3"));
        Course course3 = entityManager.persist(EntityCreatorUtil.createCourse("3", instructors));

        CourseFeedback courseFeedback3 = entityManager.persist(
                EntityCreatorUtil.createCourseFeedback("3", student3, course3)
        );

        List<CourseFeedback> courseFeedbacks = courseFeedbackRepository.findAll();

        assertThat(courseFeedbacks).hasSize(3)
                .contains(courseFeedback1, courseFeedback2, courseFeedback3);
    }

    @Transactional
    @Test
    public void should_find_course_feedback_by_id() {
        Student student1 = entityManager.persist(EntityCreatorUtil.createStudent("1"));
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructors));

        CourseFeedback courseFeedback1 = entityManager.persist(
                EntityCreatorUtil.createCourseFeedback("1", student1, course1)
        );

        Student student2 = entityManager.persist(EntityCreatorUtil.createStudent("2"));
        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructors));

        CourseFeedback courseFeedback2 = entityManager.persist(
                EntityCreatorUtil.createCourseFeedback("2", student2, course2)
        );

        Optional<CourseFeedback> foundCourseOpt = courseFeedbackRepository.findById(courseFeedback2.getId());
        assertThat(foundCourseOpt).isPresent();

        CourseFeedback foundCoursefeedback = foundCourseOpt.get();
        assertThat(foundCoursefeedback).isEqualTo(courseFeedback2);
    }

    @Transactional
    @Test
    public void should_find_course_feedbacks_by_feedback_containing_string() {
        Student student1 = entityManager.persist(EntityCreatorUtil.createStudent("1"));
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructors));

        CourseFeedback courseFeedback1 = entityManager.persist(
                EntityCreatorUtil.createCourseFeedback("1", student1, course1)
        );

        Student student2 = entityManager.persist(EntityCreatorUtil.createStudent("2"));
        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructors));

        CourseFeedback courseFeedback2ToSave = EntityCreatorUtil.createCourseFeedback("2", student2, course2);
        courseFeedback2ToSave.setFeedback("FEEDBACKtestFEEDBACKLOLOLO");
        CourseFeedback courseFeedback2 = entityManager.persist(
                courseFeedback2ToSave
        );

        Student student3 = entityManager.persist(EntityCreatorUtil.createStudent("3"));
        Course course3 = entityManager.persist(EntityCreatorUtil.createCourse("3", instructors));

        CourseFeedback courseFeedback3ToSave = EntityCreatorUtil.createCourseFeedback("3", student3, course3);
        courseFeedback3ToSave.setFeedback("FEEDBACKtetFEEDBACKLOLOLOtest");
        CourseFeedback courseFeedback3 = entityManager.persist(
                courseFeedback3ToSave
        );

        List<CourseFeedback> courseFeedbacks = courseFeedbackRepository.findByFeedbackContaining("test");

        assertThat(courseFeedbacks).hasSize(2).contains(courseFeedback2, courseFeedback3);
    }

    @Transactional
    @Test
    public void should_update_course_feedback_by_id() {
        Student student1 = entityManager.persist(EntityCreatorUtil.createStudent("1"));
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructors));

        CourseFeedback courseFeedback1 = entityManager.persist(
                EntityCreatorUtil.createCourseFeedback("1", student1, course1)
        );

        Student student2 = entityManager.persist(EntityCreatorUtil.createStudent("2"));
        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructors));

        CourseFeedback courseFeedback2 = entityManager.persist(
                EntityCreatorUtil.createCourseFeedback("2", student2, course2)
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

    @Transactional
    @Test
    public void should_delete_course_feedback_by_id() {
        Student student1 = entityManager.persist(EntityCreatorUtil.createStudent("1"));
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructors));

        CourseFeedback courseFeedback1 = entityManager.persist(
                EntityCreatorUtil.createCourseFeedback("1", student1, course1)
        );

        Student student2 = entityManager.persist(EntityCreatorUtil.createStudent("2"));
        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructors));

        CourseFeedback courseFeedback2 = entityManager.persist(
                EntityCreatorUtil.createCourseFeedback("2", student2, course2)
        );

        Student student3 = entityManager.persist(EntityCreatorUtil.createStudent("3"));
        Course course3 = entityManager.persist(EntityCreatorUtil.createCourse("3", instructors));

        CourseFeedback courseFeedback3 = entityManager.persist(
                EntityCreatorUtil.createCourseFeedback("3", student3, course3)
        );

        courseFeedbackRepository.deleteById(courseFeedback2.getId());

        List<CourseFeedback> courseFeedbacks = courseFeedbackRepository.findAll();

        assertThat(courseFeedbacks).hasSize(2).contains(courseFeedback1, courseFeedback3);
    }

    @Transactional
    @Test
    public void should_delete_all_course_feedbacks() {
        Student student1 = entityManager.persist(EntityCreatorUtil.createStudent("1"));
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructors));

        CourseFeedback courseFeedback1 = entityManager.persist(
                EntityCreatorUtil.createCourseFeedback("1", student1, course1)
        );

        Student student2 = entityManager.persist(EntityCreatorUtil.createStudent("2"));
        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructors));

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
