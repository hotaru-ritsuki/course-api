package com.example.courseapi.rest;

import com.example.courseapi.config.MockMvcBuilderTestConfiguration;
import com.example.courseapi.config.annotation.CustomMockAdmin;
import com.example.courseapi.config.annotation.CustomMockStudent;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.*;
import com.example.courseapi.dto.request.HomeworkRequestDTO;
import com.example.courseapi.dto.response.HomeworkResponseDTO;
import com.example.courseapi.repository.HomeworkRepository;
import com.example.courseapi.service.mapper.HomeworkMapper;
import com.example.courseapi.util.EntityCreatorUtil;
import com.example.courseapi.util.JacksonUtil;
import com.example.courseapi.util.TestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DefaultTestConfiguration
class HomeworkControllerTest {
    private static final String DEFAULT_FILEPATH = "HOMEWORK_FILEPATH";
    private static final String DEFAULT_TITLE = "HOMEWORK_TITLE";
    private static final String UPDATED_FILEPATH = "HOMEWORK_FILEPATH_UPDATED";
    private static final String UPDATED_TITLE = "HOMEWORK_TITLE_UPDATED";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvcBuilderTestConfiguration mockMvcBuilderTestConfiguration;

    @Autowired
    private HomeworkController homeworkController;

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Autowired
    private HomeworkMapper homeworkMapper;

    private MockMvc restHomeworkMockMvc;
    private Instructor instructor;
    private Student student;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
        this.restHomeworkMockMvc = mockMvcBuilderTestConfiguration.forControllers(homeworkController);
        this.instructor = EntityCreatorUtil.createInstructor();
        this.student =  EntityCreatorUtil.createStudent();
        this.entityManager.persist(instructor);
        this.entityManager.persist(student);
        this.entityManager.flush();
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
        Optional<Object> userOpt = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal);
        Student student;
        if (userOpt.isPresent() && userOpt.get() instanceof Student currentStudent) {
            student = currentStudent;
        } else {
            student = EntityCreatorUtil.createStudent();
            em.persist(student);
            em.flush();
        }
        homework.setStudent(student);
        Lesson lesson;
        if (TestUtil.findOne(em, Lesson.class).isEmpty()) {
            lesson = LessonControllerTest.createEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findOne(em, Lesson.class).get(0);
        }
        lesson.getCourse().addStudent(student);
        homework.setLesson(lesson);
        return homework;
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Homework createEntity(EntityManager em, Student student) {
        Homework homework = Homework.builder()
                .filePath(DEFAULT_FILEPATH)
                .title(DEFAULT_TITLE)
                .build();
        // Add required entity
        homework.setStudent(student);
        Lesson lesson;
        if (TestUtil.findOne(em, Lesson.class).isEmpty()) {
            lesson = LessonControllerTest.createEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findOne(em, Lesson.class).get(0);
        }
        homework.setLesson(lesson);
        lesson.getCourse().addStudent(student);
        return homework;
    }

    @Test
    @Transactional
    public void checkFilePathIsRequired() throws Exception {
        Homework homework = createEntity(entityManager);
        entityManager.persist(homework);
        entityManager.flush();

        long databaseSizeBeforeCreate = homeworkRepository.count();

        // set the field null
        entityManager.detach(homework);
        homework.setFilePath(null);

        // Create the Homework, which fails.
        HomeworkRequestDTO homeworkRequestDTO = homeworkMapper.toRequestDto(homework);

        restHomeworkMockMvc.perform(put("/api/v1/homeworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkRequestDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Homework in the database
        long databaseSizeAfterCreate = homeworkRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        Homework homework = createEntity(entityManager);
        entityManager.persist(homework);
        entityManager.flush();

        long databaseSizeBeforeCreate = homeworkRepository.count();

        // Create the Homework, which fails.
        HomeworkRequestDTO homeworkRequestDTO = homeworkMapper.toRequestDto(homework);
        homeworkRequestDTO.setTitle(null);

        restHomeworkMockMvc.perform(put("/api/v1/homeworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkRequestDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Homework in the database
        long databaseSizeAfterCreate = homeworkRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void checkTitle_shouldHaveLengthFrom2To50() throws Exception {
        Homework homework = createEntity(entityManager);
        entityManager.persist(homework);
        entityManager.flush();

        long databaseSizeBeforeCreate = homeworkRepository.count();

        entityManager.detach(homework);
        // set the field null
        homework.setTitle(RandomStringUtils.randomAlphabetic(1));

        // Create the Homework, which fails.
        HomeworkRequestDTO homeworkRequestDTO = homeworkMapper.toRequestDto(homework);

        restHomeworkMockMvc.perform(put("/api/v1/homeworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkRequestDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Homework in the database
        long databaseSizeAfterCreate = homeworkRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);

        homework.setTitle(RandomStringUtils.randomAlphabetic(2));

        // Create the Homework, which fails.
        homeworkRequestDTO = homeworkMapper.toRequestDto(homework);

        restHomeworkMockMvc.perform(put("/api/v1/homeworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkRequestDTO)))
                .andExpect(status().isOk());

        // Validate the Homework in the database
        databaseSizeAfterCreate = homeworkRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);

        databaseSizeBeforeCreate = homeworkRepository.count();
        homework.setTitle(RandomStringUtils.randomAlphabetic(50));

        // Create the Homework, which fails.
        homeworkRequestDTO = homeworkMapper.toRequestDto(homework);

        restHomeworkMockMvc.perform(put("/api/v1/homeworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkRequestDTO)))
                .andExpect(status().isOk());

        // Validate the Homework in the database
        databaseSizeAfterCreate = homeworkRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @CustomMockStudent
    public void getHomework() throws Exception {
        // Initialize the database
        Homework homework = homeworkRepository.saveAndFlush(createEntity(entityManager));

        // Get the homework
        restHomeworkMockMvc.perform(get("/api/v1/homeworks/{id}", homework.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(homework.getId().intValue()))
                .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
                .andExpect(jsonPath("$.filePath").value(DEFAULT_FILEPATH));
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void getNonExistingHomework() throws Exception {
        // Get the homework
        restHomeworkMockMvc.perform(get("/api/v1/homeworks/{id}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @CustomMockAdmin
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

        HomeworkRequestDTO homeworkRequestDTO = homeworkMapper.toRequestDto(updatedHomework);

        MvcResult homeworkMvcResult = restHomeworkMockMvc.perform(put("/api/v1/homeworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();

        HomeworkResponseDTO homeworkResponseDTO = JacksonUtil.deserialize(homeworkMvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        // Validate the Homework in the database
        long databaseSizeAfterUpdate = homeworkRepository.count();
        assertThat(databaseSizeAfterUpdate).isEqualTo(databaseSizeBeforeUpdate);

        Optional<Homework> savedHomeworkOpt = homeworkRepository.findById(homeworkResponseDTO.getId());
        assertThat(savedHomeworkOpt).isPresent();
        Homework savedHomework = savedHomeworkOpt.get();
        assertThat(savedHomework.getFilePath()).isNotNull();
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void updateNonExistingHomework() throws Exception {
        long databaseSizeBeforeUpdate = homeworkRepository.count();

        // Create the Homework
        HomeworkRequestDTO homeworkRequestDTO = homeworkMapper.toRequestDto(createEntity(entityManager));

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHomeworkMockMvc.perform(put("/api/v1/homeworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(homeworkRequestDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Homework in the database
        long databaseSizeAfterUpdate = homeworkRepository.count();
        assertThat(databaseSizeAfterUpdate).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void deleteHomework() throws Exception {
        // Initialize the database
        Homework homework = homeworkRepository.saveAndFlush(createEntity(entityManager));

        long databaseSizeBeforeDelete = homeworkRepository.count();

        // Delete the homework
        restHomeworkMockMvc.perform(delete("/api/v1/homeworks/{id}", homework.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database is empty
        long databaseSizeAfterDelete = homeworkRepository.count();

        assertThat(databaseSizeAfterDelete).isEqualTo(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    @CustomMockStudent
    void uploadHomeworkForLesson() throws Exception {
        Student student = (Student) TestSecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Homework homework = createEntity(entityManager, student);
        Long lessonId = homework.getLesson().getId();

        // Delete the homework
        restHomeworkMockMvc.perform(multipart("/api/v1/lesson/{lessonId}/homework", lessonId)
                        .file("file", "{\"key1\": \"value1\"}".getBytes())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    @CustomMockAdmin
    void getAllHomeworks() throws Exception {
        // Initialize the database
        Homework homework = homeworkRepository.saveAndFlush(createEntity(entityManager));

        // Get all the homeworkList
        restHomeworkMockMvc.perform(
                        get("/api/v1/homeworks?sort=id,desc&filter=id:equals:" + homework.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)));
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
        TestUtil.equalsVerifier(HomeworkResponseDTO.class);
        HomeworkResponseDTO homeworkResponseDTO1 = new HomeworkResponseDTO();
        homeworkResponseDTO1.setId(1L);
        HomeworkResponseDTO homeworkResponseDTO2 = new HomeworkResponseDTO();
        assertThat(homeworkResponseDTO1).isNotEqualTo(homeworkResponseDTO2);
        homeworkResponseDTO2.setId(homeworkResponseDTO1.getId());
        assertThat(homeworkResponseDTO1).isEqualTo(homeworkResponseDTO2);
        homeworkResponseDTO2.setId(2L);
        assertThat(homeworkResponseDTO1).isNotEqualTo(homeworkResponseDTO2);
        homeworkResponseDTO1.setId(null);
        assertThat(homeworkResponseDTO1).isNotEqualTo(homeworkResponseDTO2);
    }
}
