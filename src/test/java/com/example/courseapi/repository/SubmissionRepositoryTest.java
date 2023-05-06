package com.example.courseapi.repository;

import com.example.courseapi.config.PostgresRepositoryTestContainer;
import com.example.courseapi.config.annotation.DefaultJPARepositoryTestConfiguration;
import com.example.courseapi.domain.*;
import com.example.courseapi.util.EntityCreatorUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
@DefaultJPARepositoryTestConfiguration
public class SubmissionRepositoryTest extends PostgresRepositoryTestContainer {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

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

    @Transactional
    @Test
    public void should_find_no_submissions_if_repository_is_empty() {
        List<Submission> submissions = submissionRepository.findAll();

        assertThat(submissions).isEmpty();
    }

    @Transactional
    @Test
    public void should_store_a_submission() {
        Course course = courseRepository.save(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson = lessonRepository.save(EntityCreatorUtil.createLesson("1", course));
        Student student = userRepository.save(EntityCreatorUtil.createStudent("1"));
        Submission submission = submissionRepository.save(
                EntityCreatorUtil.createSubmission("1", lesson, student)
        );

        assertThat(submission).hasFieldOrPropertyWithValue("lesson", lesson);
        assertThat(submission).hasFieldOrPropertyWithValue("student", student);
    }

    @Transactional
    @Test
    public void should_find_all_submissions() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(EntityCreatorUtil.createStudent("1"));
        Submission submission1 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("1", lesson1, student1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Student student2 = userRepository.save(EntityCreatorUtil.createStudent("2"));
        Submission submission2 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("2", lesson2, student2)
        );

        Course course3 = entityManager.persist(EntityCreatorUtil.createCourse("3", instructor));
        Lesson lesson3 = lessonRepository.save(EntityCreatorUtil.createLesson("3", course3));
        Student student3 = userRepository.save(EntityCreatorUtil.createStudent("3"));
        Submission submission3 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("3", lesson3, student3)
        );

        List<Submission> submissions = submissionRepository.findAll();

        assertThat(submissions).hasSize(3)
                .contains(submission1, submission2, submission3);
    }

    @Transactional
    @Test
    public void should_find_submission_by_id() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(EntityCreatorUtil.createStudent("1"));
        Submission submission1 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("1", lesson1, student1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Student student2 = userRepository.save(EntityCreatorUtil.createStudent("2"));
        Submission submission2 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("2", lesson2, student2)
        );

        Optional<Submission> foundSubmissionOpt = submissionRepository
                .findBySubmissionId_LessonIdAndSubmissionId_StudentId(lesson2.getId(), student2.getId());
        assertThat(foundSubmissionOpt).isPresent();

        Submission foundSubmission = foundSubmissionOpt.get();
        assertThat(foundSubmission).isEqualTo(submission2);
    }

    @Transactional
    @Test
    public void should_find_submissions_by_student() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(EntityCreatorUtil.createStudent("1"));
        Submission submission1 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("1", lesson1, student1)
        );
        // Set student for two submissions
        Student student2 = userRepository.save(EntityCreatorUtil.createStudent("2"));

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Submission submission2 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("2", lesson2, student2)
        );

        Course course3 = entityManager.persist(EntityCreatorUtil.createCourse("3", instructor));
        Lesson lesson3 = lessonRepository.save(EntityCreatorUtil.createLesson("3", course3));
        Submission submission3 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("3", lesson3, student2)
        );

        List<Submission> submissions = submissionRepository.findAllByStudentId(student2.getId());

        assertThat(submissions).hasSize(2).contains(submission2, submission3);
    }

    @Transactional
    @Test
    public void should_find_submissions_by_lesson() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(EntityCreatorUtil.createStudent("1"));
        Submission submission1 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("1", lesson1, student1)
        );
        // Set student for two submissions
        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Student student2 = userRepository.save(EntityCreatorUtil.createStudent("2"));
        Submission submission2 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("2", lesson2, student2)
        );

        Student student3 = userRepository.save(EntityCreatorUtil.createStudent("3"));
        Submission submission3 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("3", lesson1, student3)
        );

        List<Submission> submissions = submissionRepository.findAllByLessonId(lesson1.getId());

        assertThat(submissions).hasSize(2).contains(submission1, submission3);
    }

    @Transactional
    @Test
    public void should_update_submission_by_id() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(EntityCreatorUtil.createStudent("1"));
        Submission submission1 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("1", lesson1, student1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Student student2 = userRepository.save(EntityCreatorUtil.createStudent("2"));
        Submission submission2 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("2", lesson2, student2)
        );

        Optional<Submission> submissionOpt = submissionRepository
                .findBySubmissionId_LessonIdAndSubmissionId_StudentId(lesson2.getId(), student2.getId());
        assertThat(submissionOpt).isPresent();

        Submission submission = submissionOpt.get();
        submission.setGrade(new Random().nextDouble(0.0, 100.0));
        submissionRepository.save(submission);

        Optional<Submission> checkSubmissionOpt = submissionRepository
                .findBySubmissionId_LessonIdAndSubmissionId_StudentId(lesson2.getId(), student2.getId());
        assertThat(checkSubmissionOpt).isPresent();

        Submission checkSubmission = checkSubmissionOpt.get();

        assertThat(checkSubmission.getStudent()).isEqualTo(submission.getStudent());
        assertThat(checkSubmission.getLesson()).isEqualTo(submission.getLesson());
        assertThat(checkSubmission.getGrade()).isEqualTo(submission.getGrade());
    }

    @Transactional
    @Test
    public void should_delete_submission_by_id() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(EntityCreatorUtil.createStudent("1"));
        Submission submission1 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("1", lesson1, student1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Student student2 = userRepository.save(EntityCreatorUtil.createStudent("2"));
        Submission submission2 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("2", lesson2, student2)
        );

        Course course3 = entityManager.persist(EntityCreatorUtil.createCourse("3", instructor));
        Lesson lesson3 = lessonRepository.save(EntityCreatorUtil.createLesson("3", course3));
        Student student3 = userRepository.save(EntityCreatorUtil.createStudent("3"));
        Submission submission3 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("3", lesson3, student3)
        );

        submissionRepository
                .deleteBySubmissionId_LessonIdAndSubmissionId_StudentId(lesson2.getId(), student2.getId());

        List<Submission> submissions = submissionRepository.findAll();

        assertThat(submissions).hasSize(2).contains(submission1, submission3);
    }

    @Transactional
    @Test
    public void should_delete_all_submissions() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(EntityCreatorUtil.createStudent("1"));
        Submission submission1 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("1", lesson1, student1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Student student2 = userRepository.save(EntityCreatorUtil.createStudent("2"));
        Submission submission2 = submissionRepository.save(
                EntityCreatorUtil.createSubmission("2", lesson2, student2)
        );

        submissionRepository.deleteAll();

        assertThat(submissionRepository.count()).isEqualTo(0L);
    }
}

