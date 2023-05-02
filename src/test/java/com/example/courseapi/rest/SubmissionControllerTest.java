package com.example.courseapi.rest;

import com.example.courseapi.config.MockMvcBuilderTestConfiguration;
import com.example.courseapi.config.annotation.CustomMockAdmin;
import com.example.courseapi.config.annotation.CustomMockInstructor;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.*;
import com.example.courseapi.dto.request.SubmissionRequestDTO;
import com.example.courseapi.dto.response.SubmissionResponseDTO;
import com.example.courseapi.dto.request.GradeDTO;
import com.example.courseapi.repository.SubmissionRepository;
import com.example.courseapi.service.mapper.SubmissionMapper;
import com.example.courseapi.util.EntityCreatorUtil;
import com.example.courseapi.util.JacksonUtil;
import com.example.courseapi.util.TestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DefaultTestConfiguration
class SubmissionControllerTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvcBuilderTestConfiguration mockMvcBuilderTestConfiguration;

    @Autowired
    private SubmissionController submissionController;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SubmissionMapper submissionMapper;

    private MockMvc restSubmissionMockMvc;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
        this.restSubmissionMockMvc = mockMvcBuilderTestConfiguration.forControllers(submissionController);
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Submission createEntity(EntityManager em, Double grade) {
        Lesson lesson = LessonControllerTest.createEntity(em);
        em.persist(lesson);
        em.flush();

        Student student = EntityCreatorUtil.createStudent();
        em.persist(student);
        em.flush();

        Course course = lesson.getCourse();
        course.addStudent(student);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Instructor instructor) {
            course.addInstructor(instructor);
        }
        em.persist(course);
        em.flush();

        return Submission.builder()
                .student(student)
                .lesson(lesson)
                .grade(grade)
                .build();
    }

    public static Submission createEntity(EntityManager em) {
        return createEntity(em, new Random().nextDouble(0.0, 100.0));
    }

    @Test
    @Transactional
    @CustomMockInstructor
    void createSubmission() throws Exception {
        long databaseSizeBeforeCreate = submissionRepository.count();

        // Create submission
        Submission submission = createEntity(entityManager, 80.0);
        SubmissionRequestDTO submissionRequestDTO = submissionMapper.toRequestDto(submission);

        MvcResult submissionMvcResult = restSubmissionMockMvc.perform(post("/api/v1/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(submissionRequestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId", Matchers.is(submission.getStudent().getId().intValue())))
                .andExpect(jsonPath("$.lessonId", Matchers.is(submission.getLesson().getId().intValue())))
                .andExpect(jsonPath("$.grade", Matchers.is(80.0)))
                .andReturn();

        SubmissionResponseDTO submissionResponseDTO = JacksonUtil.deserialize(submissionMvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        // Validate new Submission in the database
        long databaseSizeAfterCreate = submissionRepository.count();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);

        Optional<Submission> savedSubmissionOpt = submissionRepository.findById(
                new Submission.SubmissionId(submissionResponseDTO.getStudentId(), submissionResponseDTO.getLessonId()));
        assertThat(savedSubmissionOpt).isPresent();

        Submission savedSubmission = savedSubmissionOpt.get();
        assertThat(savedSubmission.getStudent()).isEqualTo(submission.getStudent());
        assertThat(savedSubmission.getLesson()).isEqualTo(submission.getLesson());
        assertThat(savedSubmission.getGrade()).isEqualTo(submission.getGrade());
    }

    @Test
    @Transactional
    @CustomMockInstructor
    void createSubmissionUrlSpecific() throws Exception {
        long databaseSizeBeforeCreate = submissionRepository.count();

        // Create submission
        Submission submission = createEntity(entityManager, 80.0);
        Lesson lesson = submission.getLesson();
        Student student = submission.getStudent();
        GradeDTO gradeDTO = new GradeDTO(submission.getGrade());

        MvcResult submissionMvcResult = restSubmissionMockMvc.perform(
                post("/api/v1/lesson/" + lesson.getId() + "/student/" + student.getId() + "/submission")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId", Matchers.is(student.getId().intValue())))
                .andExpect(jsonPath("$.lessonId", Matchers.is(lesson.getId().intValue())))
                .andExpect(jsonPath("$.grade", Matchers.is(80.0)))
                .andReturn();

        SubmissionResponseDTO submissionResponseDTO = JacksonUtil.deserialize(submissionMvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        // Validate new Submission in the database
        long databaseSizeAfterCreate = submissionRepository.count();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);

        Optional<Submission> savedSubmissionOpt = submissionRepository.findById(
                new Submission.SubmissionId(submissionResponseDTO.getStudentId(), submissionResponseDTO.getLessonId()));
        assertThat(savedSubmissionOpt).isPresent();

        Submission savedSubmission = savedSubmissionOpt.get();
        assertThat(savedSubmission.getStudent()).isEqualTo(submission.getStudent());
        assertThat(savedSubmission.getLesson()).isEqualTo(submission.getLesson());
        assertThat(savedSubmission.getGrade()).isEqualTo(submission.getGrade());
    }

    @Test
    @Transactional
    @CustomMockInstructor
    void findByLessonWithInstructorRole() throws Exception {

        // Create submission
        Submission submission = createEntity(entityManager, 80.0);
        submission = submissionRepository.save(submission);
        Lesson lesson = submission.getLesson();

        restSubmissionMockMvc.perform(
                get("/api/v1/lesson/" + lesson.getId() + "/submissions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    @Transactional
    @CustomMockInstructor
    void findByStudentWithInstructorRole() throws Exception {

        // Create submission
        Submission submission = createEntity(entityManager, 80.0);
        submissionRepository.save(submission);
        Student student = submission.getStudent();

        restSubmissionMockMvc.perform(
                        get("/api/v1/student/" + student.getId() + "/submissions")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    @Transactional
    @CustomMockAdmin
    void findByStudentAndLessonWithAdminRole() throws Exception {

        // Create submission
        Submission submission = createEntity(entityManager, 80.0);
        submissionRepository.save(submission);
        Student student = submission.getStudent();
        Lesson lesson = submission.getLesson();

        restSubmissionMockMvc.perform(
                        get("/api/v1/lesson/" + lesson.getId() + "/student/" + student.getId() + "/submission")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId", Matchers.is(student.getId().intValue())))
                .andExpect(jsonPath("$.lessonId", Matchers.is(lesson.getId().intValue())))
                .andExpect(jsonPath("$.grade", Matchers.is(80.0)));
    }

    @Test
    @Transactional
    @CustomMockAdmin
    void deleteByStudentAndLessonWithAdminRole() throws Exception {
        // Create submission
        Submission submission = createEntity(entityManager, 80.0);
        submissionRepository.save(submission);
        Student student = submission.getStudent();
        Lesson lesson = submission.getLesson();

        long databaseSizeBeforeDelete = submissionRepository.count();

        restSubmissionMockMvc.perform(
                        delete("/api/v1/lesson/" + lesson.getId() + "/student/" + student.getId() + "/submission")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());

        long databaseSizeAfterDelete = submissionRepository.count();
        assertThat(databaseSizeBeforeDelete - 1).isEqualTo(databaseSizeAfterDelete);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Submission.class);
        Submission submission1 = new Submission();
        submission1.setSubmissionId(new Submission.SubmissionId(1L, 1L));
        Submission submission2 = new Submission();
        submission2.setSubmissionId(submission1.getSubmissionId());
        assertThat(submission1).isEqualTo(submission2);
        submission2.setSubmissionId(new Submission.SubmissionId(2L, 2L));
        assertThat(submission1).isNotEqualTo(submission2);
        submission1.setSubmissionId(null);
        assertThat(submission1).isNotEqualTo(submission2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubmissionResponseDTO.class);
        SubmissionResponseDTO submissionResponseDTO1 = new SubmissionResponseDTO();
        submissionResponseDTO1.setStudentId(1L);
        submissionResponseDTO1.setLessonId(1L);
        SubmissionResponseDTO submissionResponseDTO2 = new SubmissionResponseDTO();
        assertThat(submissionResponseDTO1).isNotEqualTo(submissionResponseDTO2);
        submissionResponseDTO2.setStudentId(submissionResponseDTO1.getStudentId());
        submissionResponseDTO2.setLessonId(submissionResponseDTO1.getLessonId());
        assertThat(submissionResponseDTO1).isEqualTo(submissionResponseDTO2);
        submissionResponseDTO2.setStudentId(2L);
        submissionResponseDTO2.setLessonId(2L);
        assertThat(submissionResponseDTO1).isNotEqualTo(submissionResponseDTO2);
        submissionResponseDTO1.setStudentId(null);
        submissionResponseDTO1.setLessonId(null);
        assertThat(submissionResponseDTO1).isNotEqualTo(submissionResponseDTO2);
    }
}