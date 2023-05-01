package com.example.courseapi.rest;

import com.example.courseapi.config.MockMvcBuilderTestConfiguration;
import com.example.courseapi.config.annotation.CustomMockAdmin;
import com.example.courseapi.config.annotation.CustomMockInstructor;
import com.example.courseapi.config.annotation.CustomMockStudent;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.*;
import com.example.courseapi.dto.request.CourseRequestDTO;
import com.example.courseapi.dto.response.CourseResponseDTO;
import com.example.courseapi.repository.CourseRepository;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.repository.SubmissionRepository;
import com.example.courseapi.service.mapper.CourseMapper;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private MockMvcBuilderTestConfiguration mockMvcBuilderTestConfiguration;

    @Autowired
    private CourseController courseController;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private CourseMapper courseMapper;

    private MockMvc restCourseMockMvc;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
        this.restCourseMockMvc = mockMvcBuilderTestConfiguration.forControllers(courseController);
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Course createEntity(EntityManager em) {
        Course course = Course.builder()
                .title(DEFAULT_TITLE)
                .description(DEFAULT_DESCRIPTION)
                .build();
        // Add required entity
        Instructor instructor = EntityCreatorUtil.createInstructor();
        em.persist(instructor);
        em.flush();

        course.addInstructor(instructor);

        Optional<Object> userOpt = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal);
        if (userOpt.isPresent() && userOpt.get() instanceof Student student) {
            course.addStudent(student);
        }
        return course;
    }

    @Test
    @Transactional
    @CustomMockAdmin
    void createCourse() throws Exception {
        long databaseSizeBeforeCreate = courseRepository.count();

        // Create course
        CourseRequestDTO courseDTO = courseMapper.toRequestDto(createEntity(entityManager));

        MvcResult courseSaveResult = restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        CourseResponseDTO courseResponseDTO = JacksonUtil.deserialize(courseSaveResult.getResponse().getContentAsString(),
                new TypeReference<CourseResponseDTO>() {});

        // Validate new Course in the database
        long databaseSizeAfterCreate = courseRepository.count();
        assertThat(databaseSizeBeforeCreate + 1).isEqualTo(databaseSizeAfterCreate);

        Optional<Course> savedCourseOpt = courseRepository.findById(courseResponseDTO.getId());
        assertThat(savedCourseOpt).isPresent();
        Course savedCourse = savedCourseOpt.get();
        assertThat(savedCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(savedCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    void createCourseWithLessons() throws Exception {
        long databaseSizeBeforeCreate = courseRepository.count();

        // Create course
        Course course = createEntity(entityManager);
        EntityCreatorUtil.createLesson("1", course);
        EntityCreatorUtil.createLesson("2", course);
        EntityCreatorUtil.createLesson("3", course);
        EntityCreatorUtil.createLesson("4", course);
        EntityCreatorUtil.createLesson("5", course);

        CourseRequestDTO courseDTO = courseMapper.toRequestDto(course);

        MvcResult courseSaveResult = restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        // Validate new Course in the database
        CourseResponseDTO courseResponseDTO = JacksonUtil.deserialize(courseSaveResult.getResponse().getContentAsString(),
                new TypeReference<CourseResponseDTO>() {});

        // Validate new Course in the database
        long databaseSizeAfterCreate = courseRepository.count();
        assertThat(databaseSizeBeforeCreate + 1).isEqualTo(databaseSizeAfterCreate);

        Optional<Course> savedCourseOpt = courseRepository.findById(courseResponseDTO.getId());
        assertThat(savedCourseOpt).isPresent();
        Course savedCourse = savedCourseOpt.get();
        assertThat(savedCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(savedCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void createCourseWithExistingId() throws Exception {
        // Create the Course with an existing ID
        Course course = createEntity(entityManager);
        entityManager.persist(course);
        entityManager.flush();

        CourseRequestDTO courseDTO = courseMapper.toRequestDto(course);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeCreate = courseRepository.count();

        Course course = createEntity(entityManager);
        // set the field null
        course.setTitle(null);

        // Create the Course, which fails.
        CourseRequestDTO courseDTO = courseMapper.toRequestDto(course);

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
    @CustomMockAdmin
    public void checkTitle_shouldHaveLengthFrom2To100() throws Exception {
        long databaseSizeBeforeCreate = courseRepository.count();

        Course course = createEntity(entityManager);
        // set the field null
        course.setTitle(RandomStringUtils.randomAlphabetic(1));

        // Create the Course, which fails.
        CourseRequestDTO courseDTO = courseMapper.toRequestDto(course);

        restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Course in the database
        long databaseSizeAfterCreate = courseRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);

        course.setTitle(RandomStringUtils.randomAlphabetic(2));

        // Create the Course, which fails.
        courseDTO = courseMapper.toRequestDto(course);

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
        courseDTO = courseMapper.toRequestDto(course);

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

        Course course = createEntity(entityManager);
        // set the field null
        course.setDescription(null);

        // Create the Course, which fails.
        CourseRequestDTO courseDTO = courseMapper.toRequestDto(course);

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
    @CustomMockAdmin
    public void checkDescription_shouldHaveLengthFrom10To255() throws Exception {
        long databaseSizeBeforeCreate = courseRepository.count();

        Course course = createEntity(entityManager);
        // set the field null
        course.setDescription(RandomStringUtils.randomAlphabetic(1));

        // Create the Course, which fails.
        CourseRequestDTO courseDTO = courseMapper.toRequestDto(course);

        restCourseMockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Course in the database
        long databaseSizeAfterCreate = courseRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);

        course.setDescription(RandomStringUtils.randomAlphabetic(10));

        // Create the Course, which fails.
        courseDTO = courseMapper.toRequestDto(course);

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
        courseDTO = courseMapper.toRequestDto(course);

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
    @CustomMockStudent
    public void getCourse() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

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
    @CustomMockAdmin
    public void getAllCourses() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        // Get all the courseList
        restCourseMockMvc.perform(
                        get("/api/v1/courses?sort=id,desc&filter=id:equals:" + course.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)));
    }


    @Test
    @Transactional
    @CustomMockStudent
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/v1/courses/{id}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void updateCourse() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        long databaseSizeBeforeUpdate = courseRepository.count();

        // Update the course
        Optional<Course> persistedCourseOpt = courseRepository.findById(course.getId());
        assertThat(persistedCourseOpt).isPresent();
        Course persistedCourse = persistedCourseOpt.get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        entityManager.detach(persistedCourse);
        
        persistedCourse
                .setTitle(UPDATED_TITLE);
        persistedCourse
                .setDescription(UPDATED_DESCRIPTION);
        
        CourseRequestDTO courseDTO = courseMapper.toRequestDto(persistedCourse);

        restCourseMockMvc.perform(put("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isOk());

        // Validate the Course in the database
        Long databaseSizeAfterUpdate = courseRepository.count();
        assertThat(databaseSizeAfterUpdate).isEqualTo(databaseSizeBeforeUpdate);
        Optional<Course> updatedCourseOpt = courseRepository.findById(course.getId());
        assertThat(updatedCourseOpt).isPresent();
        Course updatedCourse = persistedCourseOpt.get();
        assertThat(updatedCourse.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(updatedCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void subscribeStudentToCourse() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));
        Student student = studentRepository.save(EntityCreatorUtil.createStudent());

        restCourseMockMvc.perform(put("/api/v1/courses/" + course.getId() + "/subscribe/" + student.getId()))
                .andExpect(status().isOk());

        // Validate the Course in the database
        Optional<Course> courseOpt = courseRepository.findById(course.getId());
        assertThat(courseOpt).isPresent();
        course = courseOpt.get();
        assertThat(course.getStudents()).contains(student);
    }

    @Test
    @Transactional
    @CustomMockStudent
    public void subscribeStudentToCourseWithStudentRoleShouldThrowException() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        Student student = (Student) TestSecurityContextHolder.getContext().getAuthentication().getPrincipal();

        restCourseMockMvc.perform(put("/api/v1/courses/" + course.getId() + "/subscribe/" + student.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @CustomMockStudent
    public void subscribeCurrentStudentToCourse() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));
        Student student = (Student) TestSecurityContextHolder.getContext().getAuthentication().getPrincipal();

        restCourseMockMvc.perform(put("/api/v1/courses/" + course.getId() + "/subscribe"))
                .andExpect(status().isOk());

        // Validate the Course in the database
        Optional<Course> courseOpt = courseRepository.findById(course.getId());
        assertThat(courseOpt).isPresent();
        course = courseOpt.get();
        assertThat(course.getStudents()).contains(student);
    }

    @Test
    @Transactional
    @CustomMockStudent
    public void getMyCoursesShouldReturnStudentCourses() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));
        Student student = (Student) TestSecurityContextHolder.getContext().getAuthentication().getPrincipal();

        course.addStudent(student);
        courseRepository.saveAndFlush(course);

        restCourseMockMvc.perform(get("/api/v1/courses/my"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.*.id", Matchers.hasItem(course.getId().intValue())))
                .andExpect(jsonPath("$.*.studentIds.*", Matchers.hasItem(student.getId().intValue())));

        // Validate the Course in the database
        Optional<Course> courseOpt = courseRepository.findById(course.getId());
        assertThat(courseOpt).isPresent();
        course = courseOpt.get();
        assertThat(course.getStudents()).contains(student);
    }

    @Test
    @Transactional
    @CustomMockInstructor
    public void getMyCoursesShouldReturnInstructorCourses() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));
        Instructor instructor = (Instructor) TestSecurityContextHolder.getContext().getAuthentication().getPrincipal();

        course.addInstructor(instructor);
        courseRepository.saveAndFlush(course);

        restCourseMockMvc.perform(get("/api/v1/courses/my"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.*.id", Matchers.hasItem(course.getId().intValue())))
                .andExpect(jsonPath("$.*.instructorIds.*", Matchers.hasItem(instructor.getId().intValue())));

        // Validate the Course in the database
        Optional<Course> courseOpt = courseRepository.findById(course.getId());
        assertThat(courseOpt).isPresent();
        course = courseOpt.get();
        assertThat(course.getInstructors()).contains(instructor);
    }

    @Test
    @Transactional
    @CustomMockStudent
    public void getMyCourseStatusShouldReturnCourseStatus() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        // Add lessons
        Lesson lesson1 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson2 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson3 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson4 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson5 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Student student = (Student) TestSecurityContextHolder.getContext().getAuthentication().getPrincipal();

        course.addStudent(student);
        courseRepository.saveAndFlush(course);

        // Add submissions
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 100))
                        .lesson(lesson1)
                        .student(student)
                        .build()
        );
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 100))
                        .lesson(lesson2)
                        .student(student)
                        .build()
        );
        Submission submission3 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 100))
                        .lesson(lesson3)
                        .student(student)
                        .build()
        );
        Submission submission4 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 100))
                        .lesson(lesson4)
                        .student(student)
                        .build()
        );
        Submission submission5 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 100))
                        .lesson(lesson5)
                        .student(student)
                        .build()
        );

        restCourseMockMvc.perform(get("/api/v1/courses/" + course.getId() + "/status"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", Matchers.is(course.getId().intValue())))
                .andExpect(jsonPath("$.studentIds.*", Matchers.hasItem(student.getId().intValue())))
                .andExpect(jsonPath("$.courseStatus", Matchers.notNullValue()))
                .andExpect(jsonPath("$.finalGrade", Matchers.notNullValue()));
    }

    @Test
    @Transactional
    @CustomMockStudent
    public void getMyCourseStatusShouldReturnCourseStatusCompleted() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        // Add lessons
        Lesson lesson1 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson2 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson3 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson4 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson5 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Student student = (Student) TestSecurityContextHolder.getContext().getAuthentication().getPrincipal();

        course.addStudent(student);
        courseRepository.saveAndFlush(course);

        // Add submissions
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(80, 100))
                        .lesson(lesson1)
                        .student(student)
                        .build()
        );
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(80, 100))
                        .lesson(lesson2)
                        .student(student)
                        .build()
        );
        Submission submission3 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(80, 100))
                        .lesson(lesson3)
                        .student(student)
                        .build()
        );
        Submission submission4 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(80, 100))
                        .lesson(lesson4)
                        .student(student)
                        .build()
        );
        Submission submission5 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(80, 100))
                        .lesson(lesson5)
                        .student(student)
                        .build()
        );

        restCourseMockMvc.perform(get("/api/v1/courses/" + course.getId() + "/status"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", Matchers.is(course.getId().intValue())))
                .andExpect(jsonPath("$.studentIds.*", Matchers.hasItem(student.getId().intValue())))
                .andExpect(jsonPath("$.courseStatus", Matchers.is("COMPLETED")))
                .andExpect(jsonPath("$.finalGrade", Matchers.greaterThanOrEqualTo(80.0)));
    }

    @Test
    @Transactional
    @CustomMockStudent
    public void getMyCourseStatusShouldReturnCourseStatusFailed() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        // Add lessons
        Lesson lesson1 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson2 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson3 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson4 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson5 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Student student = (Student) TestSecurityContextHolder.getContext().getAuthentication().getPrincipal();

        course.addStudent(student);
        courseRepository.saveAndFlush(course);

        // Add submissions
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson1)
                        .student(student)
                        .build()
        );
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson2)
                        .student(student)
                        .build()
        );
        Submission submission3 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson3)
                        .student(student)
                        .build()
        );
        Submission submission4 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson4)
                        .student(student)
                        .build()
        );
        Submission submission5 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson5)
                        .student(student)
                        .build()
        );

        restCourseMockMvc.perform(get("/api/v1/courses/" + course.getId() + "/status"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", Matchers.is(course.getId().intValue())))
                .andExpect(jsonPath("$.studentIds.*", Matchers.hasItem(student.getId().intValue())))
                .andExpect(jsonPath("$.courseStatus", Matchers.is("FAILED")))
                .andExpect(jsonPath("$.finalGrade", Matchers.lessThanOrEqualTo(80.0)));
    }

    @Test
    @Transactional
    @CustomMockStudent
    public void getMyCourseStatusShouldReturnCourseStatusInProgress() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        // Add lessons
        Lesson lesson1 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson2 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson3 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson4 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson5 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Student student = (Student) TestSecurityContextHolder.getContext().getAuthentication().getPrincipal();

        course.addStudent(student);
        courseRepository.saveAndFlush(course);

        // Add submissions
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson1)
                        .student(student)
                        .build()
        );
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson2)
                        .student(student)
                        .build()
        );
        Submission submission3 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson3)
                        .student(student)
                        .build()
        );
        Submission submission4 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson4)
                        .student(student)
                        .build()
        );

        restCourseMockMvc.perform(get("/api/v1/courses/" + course.getId() + "/status"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", Matchers.is(course.getId().intValue())))
                .andExpect(jsonPath("$.studentIds.*", Matchers.hasItem(student.getId().intValue())))
                .andExpect(jsonPath("$.courseStatus", Matchers.is("IN_PROGRESS")))
                .andExpect(jsonPath("$.finalGrade", Matchers.nullValue()));
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void getCourseStatusForStudentShouldReturnCourseStatus() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        // Add lessons
        Lesson lesson1 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson2 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson3 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson4 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson5 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Student student = studentRepository.save(EntityCreatorUtil.createStudent());

        course.addStudent(student);
        courseRepository.saveAndFlush(course);

        // Add submissions
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 100))
                        .lesson(lesson1)
                        .student(student)
                        .build()
        );
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 100))
                        .lesson(lesson2)
                        .student(student)
                        .build()
        );
        Submission submission3 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 100))
                        .lesson(lesson3)
                        .student(student)
                        .build()
        );
        Submission submission4 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 100))
                        .lesson(lesson4)
                        .student(student)
                        .build()
        );
        Submission submission5 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 100))
                        .lesson(lesson5)
                        .student(student)
                        .build()
        );

        restCourseMockMvc.perform(get("/api/v1/courses/" + course.getId() + "/students/" + student.getId() + "/status"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", Matchers.is(course.getId().intValue())))
                .andExpect(jsonPath("$.studentIds.*", Matchers.hasItem(student.getId().intValue())))
                .andExpect(jsonPath("$.courseStatus", Matchers.notNullValue()))
                .andExpect(jsonPath("$.finalGrade", Matchers.notNullValue()));
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void getCourseStatusForStudentShouldReturnCourseStatusCompleted() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        // Add lessons
        Lesson lesson1 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson2 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson3 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson4 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson5 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Student student = studentRepository.save(EntityCreatorUtil.createStudent());

        course.addStudent(student);
        courseRepository.saveAndFlush(course);

        // Add submissions
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(80, 100))
                        .lesson(lesson1)
                        .student(student)
                        .build()
        );
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(80, 100))
                        .lesson(lesson2)
                        .student(student)
                        .build()
        );
        Submission submission3 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(80, 100))
                        .lesson(lesson3)
                        .student(student)
                        .build()
        );
        Submission submission4 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(80, 100))
                        .lesson(lesson4)
                        .student(student)
                        .build()
        );
        Submission submission5 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(80, 100))
                        .lesson(lesson5)
                        .student(student)
                        .build()
        );

        restCourseMockMvc.perform(get("/api/v1/courses/" + course.getId() + "/students/" + student.getId() + "/status"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", Matchers.is(course.getId().intValue())))
                .andExpect(jsonPath("$.studentIds.*", Matchers.hasItem(student.getId().intValue())))
                .andExpect(jsonPath("$.courseStatus", Matchers.is("COMPLETED")))
                .andExpect(jsonPath("$.finalGrade", Matchers.greaterThanOrEqualTo(80.0)));
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void getCourseStatusForStudentShouldReturnCourseStatusFailed() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        // Add lessons
        Lesson lesson1 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson2 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson3 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson4 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson5 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Student student = studentRepository.save(EntityCreatorUtil.createStudent());

        course.addStudent(student);
        courseRepository.saveAndFlush(course);

        // Add submissions
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson1)
                        .student(student)
                        .build()
        );
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson2)
                        .student(student)
                        .build()
        );
        Submission submission3 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson3)
                        .student(student)
                        .build()
        );
        Submission submission4 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson4)
                        .student(student)
                        .build()
        );
        Submission submission5 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson5)
                        .student(student)
                        .build()
        );

        restCourseMockMvc.perform(get("/api/v1/courses/" + course.getId() + "/students/" + student.getId() + "/status"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", Matchers.is(course.getId().intValue())))
                .andExpect(jsonPath("$.studentIds.*", Matchers.hasItem(student.getId().intValue())))
                .andExpect(jsonPath("$.courseStatus", Matchers.is("FAILED")))
                .andExpect(jsonPath("$.finalGrade", Matchers.lessThanOrEqualTo(80.0)));
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void getCourseStatusForStudentShouldReturnCourseStatusInProgress() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        // Add lessons
        Lesson lesson1 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson2 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson3 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson4 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Lesson lesson5 = lessonRepository.saveAndFlush(LessonControllerTest.createEntity(course));
        Student student = studentRepository.save(EntityCreatorUtil.createStudent());

        course.addStudent(student);
        courseRepository.saveAndFlush(course);

        // Add submissions
        Submission submission1 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson1)
                        .student(student)
                        .build()
        );
        Submission submission2 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson2)
                        .student(student)
                        .build()
        );
        Submission submission3 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson3)
                        .student(student)
                        .build()
        );
        Submission submission4 = submissionRepository.save(
                Submission.builder()
                        .grade(new Random().nextDouble(0, 80))
                        .lesson(lesson4)
                        .student(student)
                        .build()
        );

        restCourseMockMvc.perform(get("/api/v1/courses/" + course.getId() + "/students/" + student.getId() + "/status"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", Matchers.is(course.getId().intValue())))
                .andExpect(jsonPath("$.studentIds.*", Matchers.hasItem(student.getId().intValue())))
                .andExpect(jsonPath("$.courseStatus", Matchers.is("IN_PROGRESS")))
                .andExpect(jsonPath("$.finalGrade", Matchers.nullValue()));
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void addInstructorToCourse() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        Instructor instructor = EntityCreatorUtil.createInstructor("1");
        entityManager.persist(instructor);
        entityManager.flush();

        restCourseMockMvc.perform(put("/api/v1/courses/" + course.getId() + "/instructor/" + instructor.getId()))
                .andExpect(status().isOk());

        Optional<Course> courseOpt = courseRepository.findById(course.getId());
        assertThat(courseOpt).isPresent();
        assertThat(courseOpt.get().getInstructors()).contains(instructor);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void deleteInstructorForCourse() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        Instructor instructor = EntityCreatorUtil.createInstructor("1");
        entityManager.persist(instructor);
        entityManager.flush();

        course.addInstructor(instructor);
        course = courseRepository.saveAndFlush(course);

        restCourseMockMvc.perform(delete("/api/v1/courses/" + course.getId() + "/instructor/" + instructor.getId()))
                .andExpect(status().isOk());

        Optional<Course> courseOpt = courseRepository.findById(course.getId());
        assertThat(courseOpt).isPresent();
        assertThat(courseOpt.get().getInstructors()).doesNotContain(instructor);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void updateNonExistingCourse() throws Exception {
        long databaseSizeBeforeUpdate = courseRepository.count();

        // Create the Course
        CourseRequestDTO courseDTO = courseMapper.toRequestDto(createEntity(entityManager));

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc.perform(put("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Course in the database
        long databaseSizeAfterUpdate = courseRepository.count();
        assertThat(databaseSizeAfterUpdate).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void deleteCourse() throws Exception {
        // Initialize the database
        Course course = courseRepository.saveAndFlush(createEntity(entityManager));

        long databaseSizeBeforeDelete = courseRepository.count();

        // Delete the course
        restCourseMockMvc.perform(delete("/api/v1/courses/{id}", course.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database is empty
        long databaseSizeAfterDelete = courseRepository.count();
        assertThat(databaseSizeAfterDelete).isEqualTo(databaseSizeBeforeDelete - 1);
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
        TestUtil.equalsVerifier(CourseResponseDTO.class);
        CourseResponseDTO courseDTO1 = new CourseResponseDTO();
        courseDTO1.setId(1L);
        CourseResponseDTO courseDTO2 = new CourseResponseDTO();
        assertThat(courseDTO1).isNotEqualTo(courseDTO2);
        courseDTO2.setId(courseDTO1.getId());
        assertThat(courseDTO1).isEqualTo(courseDTO2);
        courseDTO2.setId(2L);
        assertThat(courseDTO1).isNotEqualTo(courseDTO2);
        courseDTO1.setId(null);
        assertThat(courseDTO1).isNotEqualTo(courseDTO2);
    }
}