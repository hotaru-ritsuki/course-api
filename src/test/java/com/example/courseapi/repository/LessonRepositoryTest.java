package com.example.courseapi.repository;

import com.example.courseapi.config.DefaultRepositoryTestConfiguration;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Lesson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
@DefaultRepositoryTestConfiguration
public class LessonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private LessonRepository lessonRepository;

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
    public void should_find_no_lessons_if_repository_is_empty() {
        List<Lesson> lessons = lessonRepository.findAll();

        assertThat(lessons).isEmpty();
    }

    @Test
    public void should_store_a_lesson() {
        Course course = courseRepository.save(createCourse("1"));
        Lesson lesson = lessonRepository.save(
                Lesson.builder()
                        .title("Lesson title")
                        .description("Lesson description")
                        .course(course)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        assertThat(lesson).hasFieldOrPropertyWithValue("title", "Lesson title");
        assertThat(lesson).hasFieldOrPropertyWithValue("description", "Lesson description");
        assertThat(lesson).hasFieldOrPropertyWithValue("course", course);
    }

    @Test
    public void should_find_all_lessons() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = entityManager.persist(
                Lesson.builder()
                        .title("Title#1")
                        .description("Description#1")
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = entityManager.persist(
                Lesson.builder()
                        .title("Title#2")
                        .description("Description#2")
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course3 = entityManager.persist(createCourse("3"));
        Lesson lesson3 = entityManager.persist(
                Lesson.builder()
                        .title("Title#3")
                        .description("Description#3")
                        .course(course3)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        List<Lesson> lessons = lessonRepository.findAll();

        assertThat(lessons).hasSize(3)
                .contains(lesson1, lesson2, lesson3);
    }

    @Test
    public void should_find_lesson_by_id() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = entityManager.persist(
                Lesson.builder()
                        .title("Title#1")
                        .description("Description#1")
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = entityManager.persist(
                Lesson.builder()
                        .title("Title#2")
                        .description("Description#2")
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Optional<Lesson> foundLessonOpt = lessonRepository.findById(lesson2.getId());
        assertThat(foundLessonOpt).isPresent();

        Lesson foundLesson = foundLessonOpt.get();
        assertThat(foundLesson).isEqualTo(lesson2);
    }

    @Test
    public void should_find_lessons_by_title_containing_string() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = entityManager.persist(
                Lesson.builder()
                        .title("Title#1")
                        .description("Description#1")
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = entityManager.persist(
                Lesson.builder()
                        .title("Title#2 test")
                        .description("Description#2")
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course3 = entityManager.persist(createCourse("3"));
        Lesson lesson3 = entityManager.persist(
                Lesson.builder()
                        .title("Title#test3")
                        .description("Description#3")
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        List<Lesson> lessons = lessonRepository.findByTitleContaining("test");

        assertThat(lessons).hasSize(2).contains(lesson2, lesson3);
    }

    @Test
    public void should_find_lessons_by_description_containing_string() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = entityManager.persist(
                Lesson.builder()
                        .title("Title#1")
                        .description("Descriptesttion#1")
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = entityManager.persist(
                Lesson.builder()
                        .title("Title#2 test")
                        .description("Description#2")
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course3 = entityManager.persist(createCourse("3"));
        Lesson lesson3 = entityManager.persist(
                Lesson.builder()
                        .title("Title#test3")
                        .description("Descripttestion#3")
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        List<Lesson> lessons = lessonRepository.findByDescriptionContaining("test");

        assertThat(lessons).hasSize(2).contains(lesson1, lesson3);
    }

    @Test
    public void should_update_lesson_by_id() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = entityManager.persist(
                Lesson.builder()
                        .title("Title#1")
                        .description("Description#1")
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = entityManager.persist(
                Lesson.builder()
                        .title("Title#2")
                        .description("Description#2")
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
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

    @Test
    public void should_delete_lesson_by_id() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = entityManager.persist(
                Lesson.builder()
                        .title("Title#1")
                        .description("Description#1")
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = entityManager.persist(
                Lesson.builder()
                        .title("Title#2")
                        .description("Description#2")
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course3 = entityManager.persist(createCourse("3"));
        Lesson lesson3 = entityManager.persist(
                Lesson.builder()
                        .title("Title#3")
                        .description("Description#3")
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        lessonRepository.deleteById(lesson2.getId());

        List<Lesson> lessons = lessonRepository.findAll();

        assertThat(lessons).hasSize(2).contains(lesson1, lesson3);
    }

    @Test
    public void should_delete_all_lessons() {
        Course course1 = entityManager.persist(createCourse("1"));
        Lesson lesson1 = entityManager.persist(
                Lesson.builder()
                        .title("Title#1")
                        .description("Descriptesttion#1")
                        .course(course1)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Course course2 = entityManager.persist(createCourse("2"));
        Lesson lesson2 = entityManager.persist(
                Lesson.builder()
                        .title("Title#2 test")
                        .description("Description#2")
                        .course(course2)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        lessonRepository.deleteAll();

        assertThat(lessonRepository.count()).isEqualTo(0L);
    }
}

