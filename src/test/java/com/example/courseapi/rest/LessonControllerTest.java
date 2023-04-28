package com.example.courseapi.rest;

import com.example.courseapi.config.MockMvcBuilder;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.LessonDTO;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.service.mapper.LessonMapper;
import com.example.courseapi.util.TestUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DefaultTestConfiguration
class LessonControllerTest {
    private static final String DEFAULT_TITLE_TEXT = "LESSON_TITLE_TEXT";
    private static final String DEFAULT_DESCRIPTION_TEXT = "LESSON_DESCRIPTION_TEXT";
    private static final String UPDATED_TITLE_TEXT = "LESSON_TITLE_TEXT_UPDATED";
    private static final String UPDATED_DESCRIPTION_TEXT = "LESSON_DESCRIPTION_TEXT_UPDATED";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvcBuilder mockMvcBuilder;

    @Autowired
    private LessonController lessonController;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonMapper lessonMapper;

    private MockMvc restLessonMockMvc;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
        this.restLessonMockMvc = mockMvcBuilder.forControllers(lessonController);
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Lesson createEntity(EntityManager em) {
        Lesson lesson = Lesson.builder()
                .title(DEFAULT_TITLE_TEXT)
                .description(DEFAULT_DESCRIPTION_TEXT)
                .build();
        // Add required entity
        Course course;
        if (TestUtil.findOne(em, Course.class).isEmpty()) {
            course = CourseControllerTest.createEntity(em);
            em.persist(course);
            em.flush();
        } else {
            course = TestUtil.findOne(em, Course.class).get(0);
        }
        lesson.setCourse(course);
        return lesson;
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Lesson createEntity(Course course) {
        Lesson lesson = Lesson.builder()
                .title(DEFAULT_TITLE_TEXT)
                .description(DEFAULT_DESCRIPTION_TEXT)
                .course(course)
                .build();
        course.addLesson(lesson);
        return lesson;
    }

    @Test
    @Transactional
    void createLesson() throws Exception {
        // Create lesson
        LessonDTO lessonDTO = lessonMapper.toDto(createEntity(entityManager));

        long databaseSizeBeforeCreate = lessonRepository.count();

        restLessonMockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
                .andExpect(status().isCreated());

        // Validate new Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertEquals(lessonList.size(), databaseSizeBeforeCreate + 1);

        Lesson testLesson = lessonList.get(lessonList.size() - 1);
        assertThat(testLesson.getTitle()).isEqualTo(DEFAULT_TITLE_TEXT);
        assertThat(testLesson.getDescription()).isEqualTo(DEFAULT_DESCRIPTION_TEXT);

    }

    @Test
    @Transactional
    public void createLessonWithExistingId() throws Exception {

        // Create the Lesson with an existing ID
        LessonDTO lessonDTO = lessonMapper.toDto(createEntity(entityManager));

        long databaseSizeBeforeCreate = lessonRepository.count();

        lessonDTO.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonMockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        long databaseSizeAfterCreate = lessonRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        Lesson lesson = createEntity(entityManager);

        // Check database
        long databaseSizeBeforeCreate = lessonRepository.count();

        // set the field null
        lesson.setTitle(null);

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        long databaseSizeAfterCreate = lessonRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitle_shouldHaveLengthFrom2To100() throws Exception {
        Lesson lesson = createEntity(entityManager);

        long databaseSizeBeforeCreate = lessonRepository.count();

        // set the field null
        lesson.setTitle(RandomStringUtils.randomAlphabetic(1));

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        long databaseSizeAfterCreate = lessonRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);

        lesson.setTitle(RandomStringUtils.randomAlphabetic(2));

        // Create the Lesson, which fails.
        lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
                .andExpect(status().isCreated());

        // Validate the Lesson in the database
        databaseSizeAfterCreate = lessonRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        databaseSizeBeforeCreate = lessonRepository.count();
        lesson.setTitle(RandomStringUtils.randomAlphabetic(100));

        // Create the Lesson, which fails.
        lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
                .andExpect(status().isCreated());

        // Validate the Lesson in the database
        databaseSizeAfterCreate = lessonRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        Lesson lesson = createEntity(entityManager);

        long databaseSizeBeforeCreate = lessonRepository.count();

        // set the field null
        lesson.setDescription(null);

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        long databaseSizeAfterCreate = lessonRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescription_shouldHaveLengthFrom10To255() throws Exception {
        Lesson lesson = createEntity(entityManager);

        long databaseSizeBeforeCreate = lessonRepository.count();

        // set the field null
        lesson.setDescription(RandomStringUtils.randomAlphabetic(5));

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        long databaseSizeAfterCreate = lessonRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);

        lesson.setDescription(RandomStringUtils.randomAlphabetic(10));

        // Create the Lesson, which fails.
        lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
                .andExpect(status().isCreated());

        // Validate the Lesson in the database
        databaseSizeAfterCreate = lessonRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        databaseSizeBeforeCreate = lessonRepository.count();
        lesson.setDescription(RandomStringUtils.randomAlphabetic(255));

        // Create the Lesson, which fails.
        lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
                .andExpect(status().isCreated());

        // Validate the Lesson in the database
        databaseSizeAfterCreate = lessonRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    public void getAllLessons() throws Exception {
        // Initialize the database
        Lesson lesson = lessonRepository.saveAndFlush(createEntity(entityManager));

        // Get all the lessonList
        restLessonMockMvc.perform(
                        get("/api/v1/lessons?sort=id,desc&filter=id:equals:" + lesson.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    public void getAllLessonsForCourse() throws Exception {
        // Initialize the database
        Lesson lesson1 = lessonRepository.saveAndFlush(createEntity(entityManager));
        Lesson lesson2 = lessonRepository.saveAndFlush(createEntity(entityManager));

        Long courseId = lesson1.getCourse().getId();

        restLessonMockMvc.perform(
                        get("/api/v1/lessons/courses/" + courseId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.*.title", Matchers.hasItem(DEFAULT_TITLE_TEXT)))
                .andExpect(jsonPath("$.*.description", Matchers.hasItem(DEFAULT_DESCRIPTION_TEXT)))
                .andExpect(jsonPath("$.*.courseId", Matchers.hasItem(courseId.intValue())));
    }

    @Test
    @Transactional
    public void getLesson() throws Exception {
        // Initialize the database
        Lesson lesson = lessonRepository.saveAndFlush(createEntity(entityManager));

        // Get the lesson
        restLessonMockMvc.perform(get("/api/v1/lessons/{id}", lesson.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(lesson.getId().intValue()))
                .andExpect(jsonPath("$.title").value(DEFAULT_TITLE_TEXT))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION_TEXT));
    }

    @Test
    @Transactional
    public void getNonExistingLesson() throws Exception {
        // Get the lesson
        restLessonMockMvc.perform(get("/api/v1/lessons/{id}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLesson() throws Exception {
        // Initialize the database
        Lesson lesson = lessonRepository.saveAndFlush(createEntity(entityManager));

        long databaseSizeBeforeUpdate = lessonRepository.count();

        // Update the lesson
        Optional<Lesson> updatedLessonOpt = lessonRepository.findById(lesson.getId());
        assertThat(updatedLessonOpt).isPresent();
        Lesson updatedLesson = updatedLessonOpt.get();
        // Disconnect from session so that the updates on updatedLesson are not directly saved in db
        entityManager.detach(updatedLesson);

        updatedLesson
                .setTitle(UPDATED_TITLE_TEXT);
        updatedLesson
                .setDescription(UPDATED_DESCRIPTION_TEXT);

        LessonDTO lessonDTO = lessonMapper.toDto(updatedLesson);

        restLessonMockMvc.perform(put("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
                .andExpect(status().isOk());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize((int) databaseSizeBeforeUpdate);
        Lesson testLesson = lessonList.get(lessonList.size() - 1);
        assertThat(testLesson.getTitle()).isEqualTo(UPDATED_TITLE_TEXT);
        assertThat(testLesson.getDescription()).isEqualTo(UPDATED_DESCRIPTION_TEXT);
    }

    @Test
    @Transactional
    public void updateNonExistingLesson() throws Exception {
        // Create the Lesson
        LessonDTO lessonDTO = lessonMapper.toDto(createEntity(entityManager));

        long databaseSizeBeforeUpdate = lessonRepository.count();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonMockMvc.perform(put("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize((int)databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLesson() throws Exception {
        // Initialize the database
        Lesson lesson = lessonRepository.saveAndFlush(createEntity(entityManager));

        int databaseSizeBeforeDelete = lessonRepository.findAll().size();

        // Delete the lesson
        restLessonMockMvc.perform(delete("/api/v1/lessons/{id}", lesson.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lesson.class);
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        Lesson lesson2 = new Lesson();
        lesson2.setId(lesson1.getId());
        assertThat(lesson1).isEqualTo(lesson2);
        lesson2.setId(2L);
        assertThat(lesson1).isNotEqualTo(lesson2);
        lesson1.setId(null);
        assertThat(lesson1).isNotEqualTo(lesson2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonDTO.class);
        LessonDTO lessonDTO1 = new LessonDTO();
        lessonDTO1.setId(1L);
        LessonDTO lessonDTO2 = new LessonDTO();
        assertThat(lessonDTO1).isNotEqualTo(lessonDTO2);
        lessonDTO2.setId(lessonDTO1.getId());
        assertThat(lessonDTO1).isEqualTo(lessonDTO2);
        lessonDTO2.setId(2L);
        assertThat(lessonDTO1).isNotEqualTo(lessonDTO2);
        lessonDTO1.setId(null);
        assertThat(lessonDTO1).isNotEqualTo(lessonDTO2);
    }
}
