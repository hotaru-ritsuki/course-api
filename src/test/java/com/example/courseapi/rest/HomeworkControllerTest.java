package com.example.courseapi.rest;

import com.example.courseapi.config.MockMvcBuilder;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Homework;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.domain.Student;
import com.example.courseapi.dto.HomeworkDTO;
import com.example.courseapi.repository.HomeworkRepository;
import com.example.courseapi.service.mapper.HomeworkMapper;
import com.example.courseapi.util.TestUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DefaultTestConfiguration
class HomeworkControllerTest {
    private static final String DEFAULT_FILEPATH = "HOMEWORK_FILEPATH";
    private static final String DEFAULT_TITLE = "HOMEWORK_TITLE";
    private static final String UPDATED_FILEPATH = "HOMEWORK_FILEPATH_UPDATED";
    private static final String UPDATED_TITLE = "HOMEWORK_TITLE_UPDATED";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvcBuilder mockMvcBuilder;

    @Autowired
    private HomeworkController homeworkController;

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Autowired
    private HomeworkMapper homeworkMapper;

    private MockMvc restHomeworkMockMvc;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.restHomeworkMockMvc = mockMvcBuilder.forControllers(homeworkController);
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Homework createEntity(EntityManager em) {
        Homework homework = Homework.builder()
                .filePath(DEFAULT_FILEPATH)
                .title(DEFAULT_TITLE)
                .build();
        // Add required entity
        Student student;
        if (TestUtil.findOne(em, Student.class).isEmpty()) {
            student = UserControllerTest.createStudentEntity();
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findOne(em, Student.class).get(0);
        }
        homework.setStudent(student);
        Lesson lesson;
        if (TestUtil.findOne(em, Lesson.class).isEmpty()) {
            lesson = TestUtil.findOne(em, Lesson.class).get(0); //LessonControllerTest.createEntity();
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findOne(em, Lesson.class).get(0);
        }
        homework.setLesson(lesson);
        return homework;
    }

    @Test
    @Transactional
    void createHomework() throws Exception {
        long databaseSizeBeforeCreate = homeworkRepository.count();

        // Create homework
        HomeworkDTO homeworkDTO = homeworkMapper.toDto(createEntity(entityManager));

        restHomeworkMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkDTO)))
                .andExpect(status().isCreated());

        // Validate new Homework in the database
        List<Homework> homeworkList = homeworkRepository.findAll();
        assertEquals(homeworkList.size(), databaseSizeBeforeCreate + 1);

        Homework testHomework = homeworkList.get(homeworkList.size() - 1);
        assertThat(testHomework.getFilePath()).isEqualTo(DEFAULT_FILEPATH);

    }

    @Test
    @Transactional
    public void createHomeworkWithExistingId() throws Exception {
        long databaseSizeBeforeCreate = homeworkRepository.count();

        // Create the Homework with an existing ID
        HomeworkDTO homeworkDTO = homeworkMapper.toDto(createEntity(entityManager));
        homeworkDTO.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHomeworkMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Homework in the database
        long databaseSizeAfterCreate = homeworkRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFeedbackIsRequired() throws Exception {
        long databaseSizeBeforeCreate = homeworkRepository.count();

        Homework homework = createEntity(entityManager);
        // set the field null
        homework.setFilePath(null);

        // Create the Homework, which fails.
        HomeworkDTO homeworkDTO = homeworkMapper.toDto(homework);

        restHomeworkMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Homework in the database
        long databaseSizeAfterCreate = homeworkRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitle_shouldHaveLengthFrom10To255() throws Exception {
        long databaseSizeBeforeCreate = homeworkRepository.count();

        Homework homework = createEntity(entityManager);
        // set the field null
        homework.setFilePath(RandomStringUtils.randomAlphabetic(1));

        // Create the Homework, which fails.
        HomeworkDTO homeworkDTO = homeworkMapper.toDto(homework);

        restHomeworkMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Homework in the database
        long databaseSizeAfterCreate = homeworkRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);

        homework.setFilePath(RandomStringUtils.randomAlphabetic(10));

        // Create the Homework, which fails.
        homeworkDTO = homeworkMapper.toDto(homework);

        restHomeworkMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkDTO)))
                .andExpect(status().isCreated());

        // Validate the Homework in the database
        databaseSizeAfterCreate = homeworkRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        databaseSizeBeforeCreate = homeworkRepository.count();
        homework.setFilePath(RandomStringUtils.randomAlphabetic(255));

        // Create the Homework, which fails.
        homeworkDTO = homeworkMapper.toDto(homework);

        restHomeworkMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkDTO)))
                .andExpect(status().isCreated());

        // Validate the Homework in the database
        databaseSizeAfterCreate = homeworkRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    public void getHomework() throws Exception {
        // Initialize the database
        Homework homework = homeworkRepository.saveAndFlush(createEntity(entityManager));

        // Get the homework
        restHomeworkMockMvc.perform(get("/api/v1/course-feedbacks/{id}", homework.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(homework.getId().intValue()))
                .andExpect(jsonPath("$.feedback").value(DEFAULT_FILEPATH));
    }

    @Test
    @Transactional
    public void getNonExistingHomework() throws Exception {
        // Get the homework
        restHomeworkMockMvc.perform(get("/api/v1/course-feedbacks/{id}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHomework() throws Exception {
        // Initialize the database
        Homework homework = homeworkRepository.saveAndFlush(createEntity(entityManager));

        long databaseSizeBeforeUpdate = homeworkRepository.count();

        // Update the homework
        Optional<Homework> updatedHomeworkOpt = homeworkRepository.findById(homework.getId());
        assertThat(updatedHomeworkOpt).isPresent();
        Homework updatedHomework = updatedHomeworkOpt.get();
        // Disconnect from session so that the updates on updatedHomework are not directly saved in db
        entityManager.detach(updatedHomework);

        updatedHomework
                .setFilePath(UPDATED_FILEPATH);

        HomeworkDTO homeworkDTO = homeworkMapper.toDto(updatedHomework);

        restHomeworkMockMvc.perform(put("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkDTO)))
                .andExpect(status().isOk());

        // Validate the Homework in the database
        List<Homework> homeworkList = homeworkRepository.findAll();
        assertThat(homeworkList).hasSize((int) databaseSizeBeforeUpdate);
        Homework testHomework = homeworkList.get(homeworkList.size() - 1);
        assertThat(testHomework.getFilePath()).isEqualTo(UPDATED_FILEPATH);
    }

    @Test
    @Transactional
    public void updateNonExistingHomework() throws Exception {
        long databaseSizeBeforeUpdate = homeworkRepository.count();

        // Create the Homework
        HomeworkDTO homeworkDTO = homeworkMapper.toDto(createEntity(entityManager));

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHomeworkMockMvc.perform(put("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Homework in the database
        List<Homework> homeworkList = homeworkRepository.findAll();
        assertThat(homeworkList).hasSize((int)databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHomework() throws Exception {
        // Initialize the database
        Homework homework = homeworkRepository.saveAndFlush(createEntity(entityManager));

        int databaseSizeBeforeDelete = homeworkRepository.findAll().size();

        // Delete the homework
        restHomeworkMockMvc.perform(delete("/api/v1/course-feedbacks/{id}", homework.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Homework> homeworkList = homeworkRepository.findAll();
        assertThat(homeworkList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Homework.class);
        Homework homework1 = new Homework();
        homework1.setId(1L);
        Homework homework2 = new Homework();
        homework2.setId(homework1.getId());
        assertThat(homework1).isEqualTo(homework2);
        homework2.setId(2L);
        assertThat(homework1).isNotEqualTo(homework2);
        homework1.setId(null);
        assertThat(homework1).isNotEqualTo(homework2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HomeworkDTO.class);
        HomeworkDTO homeworkDTO1 = new HomeworkDTO();
        homeworkDTO1.setId(1L);
        HomeworkDTO homeworkDTO2 = new HomeworkDTO();
        assertThat(homeworkDTO1).isNotEqualTo(homeworkDTO2);
        homeworkDTO2.setId(homeworkDTO1.getId());
        assertThat(homeworkDTO1).isEqualTo(homeworkDTO2);
        homeworkDTO2.setId(2L);
        assertThat(homeworkDTO1).isNotEqualTo(homeworkDTO2);
        homeworkDTO1.setId(null);
        assertThat(homeworkDTO1).isNotEqualTo(homeworkDTO2);
    }
}
