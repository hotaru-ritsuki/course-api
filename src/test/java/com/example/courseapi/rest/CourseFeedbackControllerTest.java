package com.example.courseapi.rest;

import com.example.courseapi.config.MockMvcBuilder;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.domain.Student;
import com.example.courseapi.dto.CourseFeedbackDTO;
import com.example.courseapi.repository.CourseFeedbackRepository;
import com.example.courseapi.service.mapper.CourseFeedbackMapper;
import com.example.courseapi.util.EntityCreatorUtil;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DefaultTestConfiguration
class CourseFeedbackControllerTest {
    private static final String DEFAULT_FEEDBACK_TEXT = "COURSE_FEEDBACK_TEXT";
    private static final String UPDATED_FEEDBACK_TEXT = "COURSE_FEEDBACK_TEXT_UPDATED";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvcBuilder mockMvcBuilder;

    @Autowired
    private CourseFeedbackController courseFeedbackController;

    @Autowired
    private CourseFeedbackRepository courseFeedbackRepository;

    @Autowired
    private CourseFeedbackMapper courseFeedbackMapper;

    private MockMvc restCourseFeedbackMockMvc;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
        this.restCourseFeedbackMockMvc = mockMvcBuilder.forControllers(courseFeedbackController);
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static CourseFeedback createEntity(EntityManager em) {
        CourseFeedback courseFeedback = CourseFeedback.builder()
                .feedback(DEFAULT_FEEDBACK_TEXT)
                .build();
        // Add required entity
        Student student;
        if (TestUtil.findOne(em, Student.class).isEmpty()) {
            student = EntityCreatorUtil.createStudent();
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findOne(em, Student.class).get(0);
        }
        courseFeedback.setStudent(student);
        Course course;
        if (TestUtil.findOne(em, Course.class).isEmpty()) {
            course = CourseControllerTest.createEntity(em);
            em.persist(course);
            em.flush();
        } else {
            course = TestUtil.findOne(em, Course.class).get(0);
        }
        courseFeedback.setCourse(course);
        return courseFeedback;
    }

    @Test
    @Transactional
    void createCourseFeedback() throws Exception {
        long databaseSizeBeforeCreate = courseFeedbackRepository.count();

        // Create courseFeedback
        CourseFeedbackDTO courseFeedbackDTO = courseFeedbackMapper.toDto(createEntity(entityManager));

        restCourseFeedbackMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackDTO)))
                .andExpect(status().isCreated());

        // Validate new CourseFeedback in the database
        List<CourseFeedback> courseFeedbackList = courseFeedbackRepository.findAll();
        assertEquals(courseFeedbackList.size(), databaseSizeBeforeCreate + 1);

        CourseFeedback testCourseFeedback = courseFeedbackList.get(courseFeedbackList.size() - 1);
        assertThat(testCourseFeedback.getFeedback()).isEqualTo(DEFAULT_FEEDBACK_TEXT);

    }

    @Test
    @Transactional
    public void createCourseFeedbackWithExistingId() throws Exception {
        long databaseSizeBeforeCreate = courseFeedbackRepository.count();

        // Create the CourseFeedback with an existing ID
        CourseFeedbackDTO courseFeedbackDTO = courseFeedbackMapper.toDto(createEntity(entityManager));
        courseFeedbackDTO.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseFeedbackMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackDTO)))
                .andExpect(status().isBadRequest());

        // Validate the CourseFeedback in the database
        long databaseSizeAfterCreate = courseFeedbackRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFeedbackIsRequired() throws Exception {
        long databaseSizeBeforeCreate = courseFeedbackRepository.count();

        CourseFeedback courseFeedback = createEntity(entityManager);
        // set the field null
        courseFeedback.setFeedback(null);

        // Create the CourseFeedback, which fails.
        CourseFeedbackDTO courseFeedbackDTO = courseFeedbackMapper.toDto(courseFeedback);

        restCourseFeedbackMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackDTO)))
                .andExpect(status().isBadRequest());

        // Validate the CourseFeedback in the database
        long databaseSizeAfterCreate = courseFeedbackRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitle_shouldHaveLengthFrom10To255() throws Exception {
        long databaseSizeBeforeCreate = courseFeedbackRepository.count();

        CourseFeedback courseFeedback = createEntity(entityManager);
        // set the field null
        courseFeedback.setFeedback(RandomStringUtils.randomAlphabetic(1));

        // Create the CourseFeedback, which fails.
        CourseFeedbackDTO courseFeedbackDTO = courseFeedbackMapper.toDto(courseFeedback);

        restCourseFeedbackMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackDTO)))
                .andExpect(status().isBadRequest());

        // Validate the CourseFeedback in the database
        long databaseSizeAfterCreate = courseFeedbackRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);

        courseFeedback.setFeedback(RandomStringUtils.randomAlphabetic(10));

        // Create the CourseFeedback, which fails.
        courseFeedbackDTO = courseFeedbackMapper.toDto(courseFeedback);

        restCourseFeedbackMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackDTO)))
                .andExpect(status().isCreated());

        // Validate the CourseFeedback in the database
        databaseSizeAfterCreate = courseFeedbackRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        databaseSizeBeforeCreate = courseFeedbackRepository.count();
        courseFeedback.setFeedback(RandomStringUtils.randomAlphabetic(255));

        // Create the CourseFeedback, which fails.
        courseFeedbackDTO = courseFeedbackMapper.toDto(courseFeedback);

        restCourseFeedbackMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackDTO)))
                .andExpect(status().isCreated());

        // Validate the CourseFeedback in the database
        databaseSizeAfterCreate = courseFeedbackRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    public void getAllCourseFeedbacks() throws Exception {
        // Initialize the database
        CourseFeedback courseFeedback = courseFeedbackRepository.saveAndFlush(createEntity(entityManager));

        // Get all the courseFeedbackList
        restCourseFeedbackMockMvc.perform(
                get("/api/v1/course-feedbacks?sort=id,desc&filter=id:equals:" + courseFeedback.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)));
    }

    @Test
    @Transactional
    public void getCourseFeedback() throws Exception {
        // Initialize the database
        CourseFeedback courseFeedback = courseFeedbackRepository.saveAndFlush(createEntity(entityManager));

        // Get the courseFeedback
        restCourseFeedbackMockMvc.perform(get("/api/v1/course-feedbacks/{id}", courseFeedback.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(courseFeedback.getId().intValue()))
                .andExpect(jsonPath("$.feedback").value(DEFAULT_FEEDBACK_TEXT));
    }

    @Test
    @Transactional
    public void getNonExistingCourseFeedback() throws Exception {
        // Get the courseFeedback
        restCourseFeedbackMockMvc.perform(get("/api/v1/course-feedbacks/{id}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseFeedback() throws Exception {
        // Initialize the database
        CourseFeedback courseFeedback = courseFeedbackRepository.saveAndFlush(createEntity(entityManager));

        long databaseSizeBeforeUpdate = courseFeedbackRepository.count();

        // Update the courseFeedback
        Optional<CourseFeedback> updatedCourseFeedbackOpt = courseFeedbackRepository.findById(courseFeedback.getId());
        assertThat(updatedCourseFeedbackOpt).isPresent();
        CourseFeedback updatedCourseFeedback = updatedCourseFeedbackOpt.get();
        // Disconnect from session so that the updates on updatedCourseFeedback are not directly saved in db
        entityManager.detach(updatedCourseFeedback);

        updatedCourseFeedback
                .setFeedback(UPDATED_FEEDBACK_TEXT);

        CourseFeedbackDTO courseFeedbackDTO = courseFeedbackMapper.toDto(updatedCourseFeedback);

        restCourseFeedbackMockMvc.perform(put("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackDTO)))
                .andExpect(status().isOk());

        // Validate the CourseFeedback in the database
        List<CourseFeedback> courseFeedbackList = courseFeedbackRepository.findAll();
        assertThat(courseFeedbackList).hasSize((int) databaseSizeBeforeUpdate);
        CourseFeedback testCourseFeedback = courseFeedbackList.get(courseFeedbackList.size() - 1);
        assertThat(testCourseFeedback.getFeedback()).isEqualTo(UPDATED_FEEDBACK_TEXT);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseFeedback() throws Exception {
        long databaseSizeBeforeUpdate = courseFeedbackRepository.count();

        // Create the CourseFeedback
        CourseFeedbackDTO courseFeedbackDTO = courseFeedbackMapper.toDto(createEntity(entityManager));

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseFeedbackMockMvc.perform(put("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackDTO)))
                .andExpect(status().isBadRequest());

        // Validate the CourseFeedback in the database
        List<CourseFeedback> courseFeedbackList = courseFeedbackRepository.findAll();
        assertThat(courseFeedbackList).hasSize((int)databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourseFeedback() throws Exception {
        // Initialize the database
        CourseFeedback courseFeedback = courseFeedbackRepository.saveAndFlush(createEntity(entityManager));

        int databaseSizeBeforeDelete = courseFeedbackRepository.findAll().size();

        // Delete the courseFeedback
        restCourseFeedbackMockMvc.perform(delete("/api/v1/course-feedbacks/{id}", courseFeedback.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database is empty
        List<CourseFeedback> courseFeedbackList = courseFeedbackRepository.findAll();
        assertThat(courseFeedbackList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseFeedback.class);
        CourseFeedback courseFeedback1 = new CourseFeedback();
        courseFeedback1.setId(1L);
        CourseFeedback courseFeedback2 = new CourseFeedback();
        courseFeedback2.setId(courseFeedback1.getId());
        assertThat(courseFeedback1).isEqualTo(courseFeedback2);
        courseFeedback2.setId(2L);
        assertThat(courseFeedback1).isNotEqualTo(courseFeedback2);
        courseFeedback1.setId(null);
        assertThat(courseFeedback1).isNotEqualTo(courseFeedback2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseFeedbackDTO.class);
        CourseFeedbackDTO courseFeedbackDTO1 = new CourseFeedbackDTO();
        courseFeedbackDTO1.setId(1L);
        CourseFeedbackDTO courseFeedbackDTO2 = new CourseFeedbackDTO();
        assertThat(courseFeedbackDTO1).isNotEqualTo(courseFeedbackDTO2);
        courseFeedbackDTO2.setId(courseFeedbackDTO1.getId());
        assertThat(courseFeedbackDTO1).isEqualTo(courseFeedbackDTO2);
        courseFeedbackDTO2.setId(2L);
        assertThat(courseFeedbackDTO1).isNotEqualTo(courseFeedbackDTO2);
        courseFeedbackDTO1.setId(null);
        assertThat(courseFeedbackDTO1).isNotEqualTo(courseFeedbackDTO2);
    }
}
