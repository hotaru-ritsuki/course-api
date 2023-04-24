package com.example.courseapi.rest;

import com.example.courseapi.config.MockMvcBuilder;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.Course;
import com.example.courseapi.dto.CourseDTO;
import com.example.courseapi.repository.CourseRepository;
import com.example.courseapi.service.mapper.CourseMapper;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DefaultTestConfiguration
class CourseControllerTest {
    private static final String DEFAULT_TITLE = "COURSE_A_TITLE";
    private static final String DEFAULT_DESCRIPTION = "COURSE_A_DESCRIPTION";
    private static final String UPDATED_TITLE = "COURSE_B_TITLE";
    private static final String UPDATED_DESCRIPTION = "COURSE_B_DESCRIPTION";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvcBuilder mockMvcBuilder;

    @Autowired
    private CourseController courseController;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    private MockMvc restCourseMockMvc;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.restCourseMockMvc = mockMvcBuilder.forControllers(courseController);
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Course createEntity() {
        return Course.builder()
                .title(DEFAULT_TITLE)
                .description(DEFAULT_DESCRIPTION)
                .build();
    }

    @Test
    @Transactional
    void createCourse() throws Exception {
        long databaseSizeBeforeCreate = courseRepository.count();

        // Create course
        CourseDTO courseDTO = courseMapper.toDto(createEntity());

        restCourseMockMvc.perform(post("/api/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isCreated());

        // Validate new Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertEquals(courseList.size(), databaseSizeBeforeCreate + 1);

        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        long databaseSizeBeforeCreate = courseRepository.count();

        // Create the Course with an existing ID
        CourseDTO courseDTO = courseMapper.toDto(createEntity());
        courseDTO.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Course in the database
        long databaseSizeAfterCreate = courseRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeCreate = courseRepository.count();

        Course course = createEntity();
        // set the field null
        course.setTitle(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Course in the database
        long databaseSizeAfterCreate = courseRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitle_shouldHaveLengthFrom2To100() throws Exception {
        long databaseSizeBeforeCreate = courseRepository.count();

        Course course = createEntity();
        // set the field null
        course.setTitle(RandomStringUtils.randomAlphabetic(1));

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Course in the database
        long databaseSizeAfterCreate = courseRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);

        course.setTitle(RandomStringUtils.randomAlphabetic(2));

        // Create the Course, which fails.
        courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isCreated());

        // Validate the Course in the database
        databaseSizeAfterCreate = courseRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        databaseSizeBeforeCreate = courseRepository.count();
        course.setTitle(RandomStringUtils.randomAlphabetic(100));

        // Create the Course, which fails.
        courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isCreated());

        // Validate the Course in the database
        databaseSizeAfterCreate = courseRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeCreate = courseRepository.count();

        Course course = createEntity();
        // set the field null
        course.setDescription(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Course in the database
        long databaseSizeAfterCreate = courseRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescription_shouldHaveLengthFrom10To255() throws Exception {
        long databaseSizeBeforeCreate = courseRepository.count();

        Course course = createEntity();
        // set the field null
        course.setDescription(RandomStringUtils.randomAlphabetic(1));

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Course in the database
        long databaseSizeAfterCreate = courseRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);

        course.setDescription(RandomStringUtils.randomAlphabetic(10));

        // Create the Course, which fails.
        courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isCreated());

        // Validate the Course in the database
        databaseSizeAfterCreate = courseRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        databaseSizeBeforeCreate = courseRepository.count();
        course.setDescription(RandomStringUtils.randomAlphabetic(255));

        // Create the Course, which fails.
        courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isCreated());

        // Validate the Course in the database
        databaseSizeAfterCreate = courseRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);
    }


    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity());

        // Get the course
        restCourseMockMvc.perform(get("/api/v1/courses/{id}", course.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(course.getId().intValue()))
                .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/v1/courses/{id}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity());

        long databaseSizeBeforeUpdate = courseRepository.count();

        // Update the course
        Optional<Course> updatedCourseOpt = courseRepository.findById(course.getId());
        assertThat(updatedCourseOpt).isPresent();
        Course updatedCourse = updatedCourseOpt.get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        entityManager.detach(updatedCourse);
        
        updatedCourse
                .setTitle(UPDATED_TITLE);
        updatedCourse
                .setDescription(UPDATED_DESCRIPTION);
        
        CourseDTO courseDTO = courseMapper.toDto(updatedCourse);

        restCourseMockMvc.perform(put("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize((int) databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        long databaseSizeBeforeUpdate = courseRepository.count();

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(createEntity());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc.perform(put("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize((int)databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity());

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        restCourseMockMvc.perform(delete("/api/v1/courses/{id}", course.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);
        course2.setId(2L);
        assertThat(course1).isNotEqualTo(course2);
        course1.setId(null);
        assertThat(course1).isNotEqualTo(course2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseDTO.class);
        CourseDTO courseDTO1 = new CourseDTO();
        courseDTO1.setId(1L);
        CourseDTO courseDTO2 = new CourseDTO();
        assertThat(courseDTO1).isNotEqualTo(courseDTO2);
        courseDTO2.setId(courseDTO1.getId());
        assertThat(courseDTO1).isEqualTo(courseDTO2);
        courseDTO2.setId(2L);
        assertThat(courseDTO1).isNotEqualTo(courseDTO2);
        courseDTO1.setId(null);
        assertThat(courseDTO1).isNotEqualTo(courseDTO2);
    }
}