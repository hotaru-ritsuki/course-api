package com.example.courseapi.rest;

import com.example.courseapi.config.MockMvcBuilderTestConfiguration;
import com.example.courseapi.config.PostgresTestContainer;
import com.example.courseapi.config.annotation.CustomMockAdmin;
import com.example.courseapi.config.annotation.CustomMockStudent;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.domain.Student;
import com.example.courseapi.dto.request.CourseFeedbackRequestDTO;
import com.example.courseapi.dto.response.CourseFeedbackResponseDTO;
import com.example.courseapi.repository.CourseFeedbackRepository;
import com.example.courseapi.service.mapper.CourseFeedbackMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DefaultTestConfiguration
class CourseFeedbackControllerTest extends PostgresTestContainer {
    private static final String DEFAULT_FEEDBACK_TEXT = "COURSE_FEEDBACK_TEXT";
    private static final String UPDATED_FEEDBACK_TEXT = "COURSE_FEEDBACK_TEXT_UPDATED";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvcBuilderTestConfiguration mockMvcBuilderTestConfiguration;

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
        this.restCourseFeedbackMockMvc = mockMvcBuilderTestConfiguration.forControllers(courseFeedbackController);
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
        Optional<Object> userOpt = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal);
        if (userOpt.isPresent() && userOpt.get() instanceof Student student) {
            courseFeedback.setStudent(student);
        } else {
            Student student = EntityCreatorUtil.createStudent();
            em.persist(student);
            em.flush();
            courseFeedback.setStudent(student);
        }
        Course course = CourseControllerTest.createEntity(em);
        em.persist(course);
        em.flush();
        courseFeedback.setCourse(course);
        return courseFeedback;
    }

    @Test
    @Transactional
    @CustomMockStudent
    void createCourseFeedback() throws Exception {
        long databaseSizeBeforeCreate = courseFeedbackRepository.count();

        // Create courseFeedback
        CourseFeedbackRequestDTO courseFeedbackRequestDTO = courseFeedbackMapper.toRequestDto(createEntity(entityManager));

        MvcResult courseFeedbackMvcresult = restCourseFeedbackMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        CourseFeedbackResponseDTO courseFeedbackResponseDTO = JacksonUtil.deserialize(courseFeedbackMvcresult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        // Validate new CourseFeedback in the database
        long databaseSizeAfterCreate = courseFeedbackRepository.count();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);
        Optional<CourseFeedback> courseFeedbackOpt = courseFeedbackRepository.findById(courseFeedbackResponseDTO.getId());
        assertThat(courseFeedbackOpt).isPresent();
        CourseFeedback courseFeedback = courseFeedbackOpt.get();
        assertThat(courseFeedback.getFeedback()).isEqualTo(DEFAULT_FEEDBACK_TEXT);

    }

    @Test
    @Transactional
    @CustomMockStudent
    public void createCourseFeedbackWithExistingId() throws Exception {
        long databaseSizeBeforeCreate = courseFeedbackRepository.count();

        // Create the CourseFeedback with an existing ID
        CourseFeedbackResponseDTO courseFeedbackResponseDTO = courseFeedbackMapper.toResponseDto(createEntity(entityManager));
        courseFeedbackResponseDTO.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseFeedbackMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackResponseDTO)))
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
        CourseFeedbackResponseDTO courseFeedbackResponseDTO = courseFeedbackMapper.toResponseDto(courseFeedback);

        restCourseFeedbackMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackResponseDTO)))
                .andExpect(status().isBadRequest());

        // Validate the CourseFeedback in the database
        long databaseSizeAfterCreate = courseFeedbackRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @CustomMockStudent
    public void checkTitle_shouldHaveLengthFrom10To255() throws Exception {
        long databaseSizeBeforeCreate = courseFeedbackRepository.count();

        CourseFeedback courseFeedback = createEntity(entityManager);
        // set the field null
        courseFeedback.setFeedback(RandomStringUtils.randomAlphabetic(1));

        // Create the CourseFeedback, which fails.
        CourseFeedbackResponseDTO courseFeedbackResponseDTO = courseFeedbackMapper.toResponseDto(courseFeedback);

        restCourseFeedbackMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackResponseDTO)))
                .andExpect(status().isBadRequest());

        // Validate the CourseFeedback in the database
        long databaseSizeAfterCreate = courseFeedbackRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);

        courseFeedback.setFeedback(RandomStringUtils.randomAlphabetic(10));

        // Create the CourseFeedback, which fails.
        courseFeedbackResponseDTO = courseFeedbackMapper.toResponseDto(courseFeedback);

        restCourseFeedbackMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackResponseDTO)))
                .andExpect(status().isCreated());

        // Validate the CourseFeedback in the database
        databaseSizeAfterCreate = courseFeedbackRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        databaseSizeBeforeCreate = courseFeedbackRepository.count();
        courseFeedback.setFeedback(RandomStringUtils.randomAlphabetic(255));

        // Create the CourseFeedback, which fails.
        courseFeedbackResponseDTO = courseFeedbackMapper.toResponseDto(courseFeedback);

        restCourseFeedbackMockMvc.perform(post("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackResponseDTO)))
                .andExpect(status().isCreated());

        // Validate the CourseFeedback in the database
        databaseSizeAfterCreate = courseFeedbackRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    @CustomMockAdmin
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
    @CustomMockAdmin
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
    @CustomMockStudent
    public void getNonExistingCourseFeedback() throws Exception {
        // Get the courseFeedback
        restCourseFeedbackMockMvc.perform(get("/api/v1/course-feedbacks/{id}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @CustomMockStudent
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

        CourseFeedbackResponseDTO courseFeedbackResponseDTO = courseFeedbackMapper.toResponseDto(updatedCourseFeedback);

        restCourseFeedbackMockMvc.perform(put("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackResponseDTO)))
                .andExpect(status().isOk());

        // Validate the CourseFeedback in the database

        // Validate new CourseFeedback in the database
        long databaseSizeAfterUpdate = courseFeedbackRepository.count();
        assertThat(databaseSizeAfterUpdate).isEqualTo(databaseSizeBeforeUpdate);
        Optional<CourseFeedback> courseFeedbackOpt = courseFeedbackRepository.findById(courseFeedbackResponseDTO.getId());
        assertThat(courseFeedbackOpt).isPresent();
        CourseFeedback courseFeedbackAfterUpdate = courseFeedbackOpt.get();
        assertThat(courseFeedbackAfterUpdate.getFeedback()).isEqualTo(UPDATED_FEEDBACK_TEXT);
    }

    @Test
    @Transactional
    @CustomMockStudent
    public void updateNonExistingCourseFeedback() throws Exception {
        long databaseSizeBeforeUpdate = courseFeedbackRepository.count();

        // Create the CourseFeedback
        CourseFeedbackResponseDTO courseFeedbackResponseDTO = courseFeedbackMapper.toResponseDto(createEntity(entityManager));

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseFeedbackMockMvc.perform(put("/api/v1/course-feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackResponseDTO)))
                .andExpect(status().isBadRequest());

        // Validate the CourseFeedback in the database
        long databaseSizeAfterUpdate = courseFeedbackRepository.count();

        assertThat(databaseSizeAfterUpdate).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void deleteCourseFeedback() throws Exception {
        // Initialize the database
        CourseFeedback courseFeedback = courseFeedbackRepository.saveAndFlush(createEntity(entityManager));

        long databaseSizeBeforeDelete = courseFeedbackRepository.count();

        // Delete the courseFeedback
        restCourseFeedbackMockMvc.perform(delete("/api/v1/course-feedbacks/{id}", courseFeedback.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database is empty

        long databaseSizeAfterDelete = courseFeedbackRepository.count();
        assertThat(databaseSizeAfterDelete).isEqualTo(databaseSizeBeforeDelete - 1);
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
        TestUtil.equalsVerifier(CourseFeedbackResponseDTO.class);
        CourseFeedbackResponseDTO courseFeedbackResponseDTO1 = new CourseFeedbackResponseDTO();
        courseFeedbackResponseDTO1.setId(1L);
        CourseFeedbackResponseDTO courseFeedbackResponseDTO2 = new CourseFeedbackResponseDTO();
        assertThat(courseFeedbackResponseDTO1).isNotEqualTo(courseFeedbackResponseDTO2);
        courseFeedbackResponseDTO2.setId(courseFeedbackResponseDTO1.getId());
        assertThat(courseFeedbackResponseDTO1).isEqualTo(courseFeedbackResponseDTO2);
        courseFeedbackResponseDTO2.setId(2L);
        assertThat(courseFeedbackResponseDTO1).isNotEqualTo(courseFeedbackResponseDTO2);
        courseFeedbackResponseDTO1.setId(null);
        assertThat(courseFeedbackResponseDTO1).isNotEqualTo(courseFeedbackResponseDTO2);
    }
}
