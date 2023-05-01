package com.example.courseapi.repository;

import com.example.courseapi.config.annotation.DefaultJPARepositoryTestConfiguration;
import com.example.courseapi.domain.*;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.util.EntityCreatorUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
@DefaultJPARepositoryTestConfiguration
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
        Course course = courseRepository.save(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson = lessonRepository.save(EntityCreatorUtil.createLesson("1", course));
        Student student = userRepository.save(createStudent("1"));
        Homework homework = homeworkRepository.save(
                EntityCreatorUtil.createHomework("1", lesson, student)
        );

        assertThat(homework).hasFieldOrPropertyWithValue("title", "Title#1");
        assertThat(homework).hasFieldOrPropertyWithValue("filePath", "Filepath#1");
        assertThat(homework).hasFieldOrPropertyWithValue("lesson", lesson);
        assertThat(homework).hasFieldOrPropertyWithValue("student", student);
    }

    @Test
    public void should_find_all_homeworks() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Homework homework1 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("1", lesson1, student1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Student student2 = userRepository.save(createStudent("2"));
        Homework homework2 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("2", lesson2, student2)
        );

        Course course3 = entityManager.persist(EntityCreatorUtil.createCourse("3", instructor));
        Lesson lesson3 = lessonRepository.save(EntityCreatorUtil.createLesson("3", course3));
        Student student3 = userRepository.save(createStudent("3"));
        Homework homework3 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("3", lesson3, student3)
        );

        List<Homework> homeworks = homeworkRepository.findAll();

        assertThat(homeworks).hasSize(3)
                .contains(homework1, homework2, homework3);
    }

    @Test
    public void should_find_homework_by_id() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Homework homework1 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("1", lesson1, student1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Student student2 = userRepository.save(createStudent("2"));
        Homework homework2 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("2", lesson2, student2)
        );

        Optional<Homework> foundHomeworkOpt = homeworkRepository.findById(homework2.getId());
        assertThat(foundHomeworkOpt).isPresent();

        Homework foundHomework = foundHomeworkOpt.get();
        assertThat(foundHomework).isEqualTo(homework2);
    }

    @Test
    public void should_find_homeworks_by_title_containing_string() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Homework homework1 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("1", lesson1, student1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Student student2 = userRepository.save(createStudent("2"));
        Homework homework2ToSave = EntityCreatorUtil.createHomework("2", lesson2, student2);
        homework2ToSave.setTitle("TitletestTile");
        Homework homework2 = homeworkRepository.save(homework2ToSave);

        Course course3 = entityManager.persist(EntityCreatorUtil.createCourse("3", instructor));
        Lesson lesson3 = lessonRepository.save(EntityCreatorUtil.createLesson("3", course3));
        Student student3 = userRepository.save(createStudent("3"));
        Homework homework3ToSave = EntityCreatorUtil.createHomework("3", lesson3, student3);
        homework3ToSave.setTitle("TitleteTiletest");
        Homework homework3 = homeworkRepository.save(homework3ToSave);

        List<Homework> homeworks = homeworkRepository.findByTitleContaining("test");

        assertThat(homeworks).hasSize(2).contains(homework2, homework3);
    }

    @Test
    public void should_update_homework_by_id() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Homework homework1 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("1", lesson1, student1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Student student2 = userRepository.save(createStudent("2"));
        Homework homework2 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("2", lesson2, student2)
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
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Homework homework1 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("1", lesson1, student1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Student student2 = userRepository.save(createStudent("2"));
        Homework homework2 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("2", lesson2, student2)
        );

        Course course3 = entityManager.persist(EntityCreatorUtil.createCourse("3", instructor));
        Lesson lesson3 = lessonRepository.save(EntityCreatorUtil.createLesson("3", course3));
        Student student3 = userRepository.save(createStudent("3"));
        Homework homework3 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("3", lesson3, student3)
        );

        homeworkRepository.deleteById(homework2.getId());

        List<Homework> homeworks = homeworkRepository.findAll();

        assertThat(homeworks).hasSize(2).contains(homework1, homework3);
    }

    @Test
    public void should_delete_all_homeworks() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = lessonRepository.save(EntityCreatorUtil.createLesson("1", course1));
        Student student1 = userRepository.save(createStudent("1"));
        Homework homework1 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("1", lesson1, student1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = lessonRepository.save(EntityCreatorUtil.createLesson("2", course2));
        Student student2 = userRepository.save(createStudent("2"));
        Homework homework2 = homeworkRepository.save(
                EntityCreatorUtil.createHomework("2", lesson2, student2)
        );

        homeworkRepository.deleteAll();

        assertThat(homeworkRepository.count()).isEqualTo(0L);
    }
}

