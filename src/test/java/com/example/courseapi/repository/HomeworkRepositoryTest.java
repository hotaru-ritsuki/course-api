package com.example.courseapi.repository;

import com.example.courseapi.config.DefaultRepositoryTestConfiguration;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.domain.Homework;
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
public class HomeworkRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private HomeworkRepository homeworkRepository;

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
    public void should_find_no_homeworks_if_repository_is_empty() {
        List<Homework> homeworks = homeworkRepository.findAll();

        assertThat(homeworks).isEmpty();
    }

    @Test
    public void should_store_a_homework() {
        Course course = courseRepository.save(createCourse("1"));
        Lesson lesson = lessonRepository.save(createLesson("1", course));
        Student student = userRepository.save(createStudent("1"));
        Homework homework = homeworkRepository.save(
                Homework.builder()
                        .title("Homework title")
                        .filePath("Homework filepath")
                        .lesson(lesson)
                        .student(student)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        assertThat(homework).hasFieldOrPropertyWithValue("title", "Homework title");
        assertThat(homework).hasFieldOrPropertyWithValue("filePath", "Homework filepath");
        assertThat(homework).hasFieldOrPropertyWithValue("lesson", lesson);
        assertThat(homework).hasFieldOrPropertyWithValue("student", student);
    }

    @Test
    public void should_find_all_homeworks() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Homework homework1 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#1")
                        .filePath("Filepath#1")
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
        Homework homework2 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#2")
                        .filePath("Filepath#2")
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
        Homework homework3 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#3")
                        .filePath("Filepath#3")
                        .lesson(lesson3)
                        .student(student3)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        List<Homework> homeworks = homeworkRepository.findAll();

        assertThat(homeworks).hasSize(3)
                .contains(homework1, homework2, homework3);
    }

    @Test
    public void should_find_homework_by_id() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Homework homework1 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#1")
                        .filePath("Filepath#1")
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
        Homework homework2 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#2")
                        .filePath("Filepath#2")
                        .lesson(lesson2)
                        .student(student2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Optional<Homework> foundHomeworkOpt = homeworkRepository.findById(homework2.getId());
        assertThat(foundHomeworkOpt).isPresent();

        Homework foundHomework = foundHomeworkOpt.get();
        assertThat(foundHomework).isEqualTo(homework2);
    }

    @Test
    public void should_find_homeworks_by_title_containing_string() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Homework homework1 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#1")
                        .filePath("Filepath#1")
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
        Homework homework2 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#2test")
                        .filePath("Filepath#2")
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
        Homework homework3 = homeworkRepository.save(
                Homework.builder()
                        .title("Tittestle#3")
                        .filePath("Filepath#3")
                        .lesson(lesson3)
                        .student(student3)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        List<Homework> homeworks = homeworkRepository.findByTitleContaining("test");

        assertThat(homeworks).hasSize(2).contains(homework2, homework3);
    }

    @Test
    public void should_update_homework_by_id() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Homework homework1 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#1")
                        .filePath("Filepath#1")
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
        Homework homework2 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#2")
                        .filePath("Filepath#2")
                        .lesson(lesson2)
                        .student(student2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Optional<Homework> homeworkOpt = homeworkRepository.findById(homework2.getId());
        assertThat(homeworkOpt).isPresent();

        Homework homework = homeworkOpt.get();
        homework.setTitle("UPDATED TITLE");
        homework.setFilePath("UPDATED FILEPATH");
        homeworkRepository.save(homework);

        Optional<Homework> checkHomeworkOpt = homeworkRepository.findById(homework2.getId());
        assertThat(checkHomeworkOpt).isPresent();

        Homework checkHomework = checkHomeworkOpt.get();

        assertThat(checkHomework.getId()).isEqualTo(homework.getId());
        assertThat(checkHomework.getTitle()).isEqualTo(homework.getTitle());
        assertThat(checkHomework.getFilePath()).isEqualTo(homework.getFilePath());
    }

    @Test
    public void should_delete_homework_by_id() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Homework homework1 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#1")
                        .filePath("Filepath#1")
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
        Homework homework2 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#2")
                        .filePath("Filepath#2")
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
        Homework homework3 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#3")
                        .filePath("Filepath#3")
                        .lesson(lesson3)
                        .student(student3)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        homeworkRepository.deleteById(homework2.getId());

        List<Homework> homeworks = homeworkRepository.findAll();

        assertThat(homeworks).hasSize(2).contains(homework1, homework3);
    }

    @Test
    public void should_delete_all_homeworks() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = lessonRepository.save(createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Homework homework1 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#1")
                        .filePath("Filepath#1")
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
        Homework homework2 = homeworkRepository.save(
                Homework.builder()
                        .title("Title#2")
                        .filePath("Filepath#2")
                        .lesson(lesson1)
                        .student(student1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        homeworkRepository.deleteAll();

        assertThat(homeworkRepository.count()).isEqualTo(0L);
    }
}

