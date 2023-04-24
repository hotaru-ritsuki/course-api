package com.example.courseapi.repository;

import com.example.courseapi.config.DefaultRepositoryTestConfiguration;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Submission;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.enums.Roles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
@DefaultRepositoryTestConfiguration
public class SubmissionRepositoryTest {

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

    private static Lesson createLesson(String uuid, Course course) {
        return Lesson.builder()
                .description("Lesson description #" + uuid)
                .title("Lesson title #" +uuid)
                .course(course)
                .createdBy("Anonymous")
                .createdDate(LocalDateTime.now())
                .modifiedBy("Anonymous")
                .modifiedDate(LocalDateTime.now())
                .build();
    }

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


    @Test
    public void should_find_no_submissions_if_repository_is_empty() {
        List<Submission> submissions = submissionRepository.findAll();

        assertThat(submissions).isEmpty();
    }

    @Test
    public void should_store_a_submission() {
        Course course = courseRepository.save(createCourse("1"));
        Lesson lesson = lessonRepository.save(createLesson("1", course));
        Student student = userRepository.save(createStudent("1"));
        Double grade = new Random().nextDouble(0.0, 100.0);
        Submission submission = submissionRepository.save(
                Submission.builder()
                        .grade(grade)
                        .lesson(lesson)
                        .student(student)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        assertThat(submission).hasFieldOrPropertyWithValue("grade", grade);
        assertThat(submission).hasFieldOrPropertyWithValue("lesson", lesson);
        assertThat(submission).hasFieldOrPropertyWithValue("student", student);
    }

    @Test
    public void should_find_all_submissions() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson1)
                        .student(student1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = lessonRepository.save(createLesson("2", course2));
        Student student2 = userRepository.save(createStudent("2"));
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson2)
                        .student(student2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course3 = entityManager.persist(createCourse("3"));
        Lesson lesson3 = lessonRepository.save(createLesson("3", course3));
        Student student3 = userRepository.save(createStudent("3"));
        Submission submission3 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson3)
                        .student(student3)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        List<Submission> submissions = submissionRepository.findAll();

        assertThat(submissions).hasSize(3)
                .contains(submission1, submission2, submission3);
    }

    @Test
    public void should_find_submission_by_id() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson1)
                        .student(student1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = lessonRepository.save(createLesson("2", course2));
        Student student2 = userRepository.save(createStudent("2"));
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson2)
                        .student(student2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Optional<Submission> foundSubmissionOpt = submissionRepository
                .findBySubmissionId_LessonIdAndSubmissionId_StudentId(lesson2.getId(), student2.getId());
        assertThat(foundSubmissionOpt).isPresent();

        Submission foundSubmission = foundSubmissionOpt.get();
        assertThat(foundSubmission).isEqualTo(submission2);
    }

    @Test
    public void should_find_submissions_by_student() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson1)
                        .student(student1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );
        // Set student for two submissions
        Student student2 = userRepository.save(createStudent("2"));

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = lessonRepository.save(createLesson("2", course2));
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson2)
                        .student(student2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course3 = entityManager.persist(createCourse("3"));
        Lesson lesson3 = lessonRepository.save(createLesson("3", course3));
        Submission submission3 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson3)
                        .student(student2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        List<Submission> submissions = submissionRepository.findAllByStudentId(student2.getId());

        assertThat(submissions).hasSize(2).contains(submission2, submission3);
    }

    @Test
    public void should_find_submissions_by_lesson() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson1)
                        .student(student1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );
        // Set student for two submissions
        Student student2 = userRepository.save(createStudent("2"));
        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = lessonRepository.save(createLesson("2", course2));
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson2)
                        .student(student2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Student student3 = userRepository.save(createStudent("3"));
        Submission submission3 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson1)
                        .student(student3)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        List<Submission> submissions = submissionRepository.findAllByLessonId(lesson1.getId());

        assertThat(submissions).hasSize(2).contains(submission1, submission3);
    }

    @Test
    public void should_update_submission_by_id() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson1)
                        .student(student1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = lessonRepository.save(createLesson("2", course2));
        Student student2 = userRepository.save(createStudent("2"));
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson2)
                        .student(student2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
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

    @Test
    public void should_delete_submission_by_id() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson1)
                        .student(student1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = lessonRepository.save(createLesson("2", course2));
        Student student2 = userRepository.save(createStudent("2"));
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson2)
                        .student(student2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course3 = entityManager.persist(createCourse("3"));
        Lesson lesson3 = lessonRepository.save(createLesson("3", course3));
        Student student3 = userRepository.save(createStudent("3"));
        Submission submission3 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson3)
                        .student(student3)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        submissionRepository
                .deleteBySubmissionId_LessonIdAndSubmissionId_StudentId(lesson2.getId(), student2.getId());

        List<Submission> submissions = submissionRepository.findAll();

        assertThat(submissions).hasSize(2).contains(submission1, submission3);
    }

    @Test
    public void should_delete_all_submissions() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson1)
                        .student(student1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = lessonRepository.save(createLesson("2", course2));
        Student student2 = userRepository.save(createStudent("2"));
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0.0, 100.0))
                        .lesson(lesson1)
                        .student(student1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        submissionRepository.deleteAll();

        assertThat(submissionRepository.count()).isEqualTo(0L);
    }
}

