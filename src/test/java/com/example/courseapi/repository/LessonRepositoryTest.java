package com.example.courseapi.repository;

import com.example.courseapi.config.PostgresRepositoryTestContainer;
import com.example.courseapi.config.annotation.DefaultJPARepositoryTestConfiguration;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Instructor;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.util.EntityCreatorUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
@DefaultJPARepositoryTestConfiguration
public class LessonRepositoryTest extends PostgresRepositoryTestContainer {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private LessonRepository lessonRepository;

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
    public void should_find_no_lessons_if_repository_is_empty() {
        List<Lesson> lessons = lessonRepository.findAll();

        assertThat(lessons).isEmpty();
    }

    @Transactional
    @Test
    public void should_store_a_lesson() {
        Course course = courseRepository.save(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson = lessonRepository.save(
                EntityCreatorUtil.createLesson("1", course)
        );

        assertThat(lesson).hasFieldOrPropertyWithValue("title", "Title#1");
        assertThat(lesson).hasFieldOrPropertyWithValue("description", "Description#1");
        assertThat(lesson).hasFieldOrPropertyWithValue("course", course);
    }

    @Transactional
    @Test
    public void should_find_all_lessons() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = entityManager.persist(
                EntityCreatorUtil.createLesson("1", course1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = entityManager.persist(
                EntityCreatorUtil.createLesson("2", course2)
        );

        Course course3 = entityManager.persist(EntityCreatorUtil.createCourse("3", instructor));
        Lesson lesson3 = entityManager.persist(
                EntityCreatorUtil.createLesson("3", course3)
        );

        List<Lesson> lessons = lessonRepository.findAll();

        assertThat(lessons).hasSize(3)
                .contains(lesson1, lesson2, lesson3);
    }

    @Transactional
    @Test
    public void should_find_lesson_by_id() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = entityManager.persist(
                EntityCreatorUtil.createLesson("1", course1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = entityManager.persist(
                EntityCreatorUtil.createLesson("2", course2)
        );

        Optional<Lesson> foundLessonOpt = lessonRepository.findById(lesson2.getId());
        assertThat(foundLessonOpt).isPresent();

        Lesson foundLesson = foundLessonOpt.get();
        assertThat(foundLesson).isEqualTo(lesson2);
    }

    @Transactional
    @Test
    public void should_find_lessons_by_title_containing_string() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = entityManager.persist(
                EntityCreatorUtil.createLesson("1", course1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2ToSave = EntityCreatorUtil.createLesson("2", course2);
        lesson2ToSave.setTitle("TESTTITLEtest");
        Lesson lesson2 = entityManager.persist(
                lesson2ToSave
        );

        Course course3 = entityManager.persist(EntityCreatorUtil.createCourse("3", instructor));
        Lesson lesson3ToSave = EntityCreatorUtil.createLesson("3", course3);
        lesson3ToSave.setTitle("OMEGAtestOMEGA");
        Lesson lesson3 = entityManager.persist(
                lesson3ToSave
        );

        List<Lesson> lessons = lessonRepository.findByTitleContaining("test");

        assertThat(lessons).hasSize(2).contains(lesson2, lesson3);
    }

    @Transactional
    @Test
    public void should_find_lessons_by_description_containing_string() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1ToSave = EntityCreatorUtil.createLesson("1", course1);
        lesson1ToSave.setDescription("TESTTITLEtestDescription");
        Lesson lesson1 = entityManager.persist(
                lesson1ToSave
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = entityManager.persist(
                EntityCreatorUtil.createLesson("2", course2)
        );

        Course course3 = entityManager.persist(EntityCreatorUtil.createCourse("3", instructor));
        Lesson lesson3ToSave = EntityCreatorUtil.createLesson("3", course3);
        lesson3ToSave.setDescription("stDescriptiontest");
        Lesson lesson3 = entityManager.persist(
                lesson3ToSave
        );

        List<Lesson> lessons = lessonRepository.findByDescriptionContaining("test");

        assertThat(lessons).hasSize(2).contains(lesson1, lesson3);
    }

    @Transactional
    @Test
    public void should_update_lesson_by_id() {
        Course course1 = entityManager.persist(EntityCreatorUtil.createCourse("1", instructor));
        Lesson lesson1 = entityManager.persist(
                EntityCreatorUtil.createLesson("1", course1)
        );

        Course course2 = entityManager.persist(EntityCreatorUtil.createCourse("2", instructor));
        Lesson lesson2 = entityManager.persist(
                EntityCreatorUtil.createLesson("2", course2)
        );

        Optional<Lesson> lessonOpt = lessonRepository.findById(lesson2.getId());
        assertThat(lessonOpt).isPresent();

        Lesson lesson = lessonOpt.get();
        lesson.setTitle("UPDATED TITLE");
        lesson.setDescription("UPDATED DESCRIPTION");
        lessonRepository.save(lesson);

        Optional<Lesson> checkLessonOpt = lessonRepository.findById(lesson2.getId());
        assertThat(checkLessonOpt).isPresent();

        Lesson checkLesson = checkLessonOpt.get();

        assertThat(checkLesson.getId()).isEqualTo(lesson.getId());
        assertThat(checkLesson.getTitle()).isEqualTo(lesson.getTitle());
        assertThat(checkLesson.getDescription()).isEqualTo(lesson.getDescription());
    }
}

