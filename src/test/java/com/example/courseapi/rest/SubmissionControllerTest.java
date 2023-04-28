package com.example.courseapi.rest;

import com.example.courseapi.config.MockMvcBuilder;
import com.example.courseapi.config.annotation.CustomMockAdmin;
import com.example.courseapi.config.annotation.CustomMockInstructor;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.*;
import com.example.courseapi.dto.GradeDTO;
import com.example.courseapi.dto.SubmissionDTO;
import com.example.courseapi.repository.SubmissionRepository;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.service.mapper.SubmissionMapper;
import com.example.courseapi.util.EntityCreatorUtil;
import com.example.courseapi.util.TestUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DefaultTestConfiguration
class SubmissionControllerTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvcBuilder mockMvcBuilder;

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
        this.restSubmissionMockMvc = mockMvcBuilder.forControllers(submissionController);
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Submission createEntity(EntityManager em, Double grade) {
        Lesson lesson;
        if (TestUtil.findOne(em, Lesson.class).isEmpty()) {
            lesson = LessonControllerTest.createEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findOne(em, Lesson.class).get(0);
        }
        Student student;
        if (TestUtil.findOne(em, Student.class).isEmpty()) {
            student = EntityCreatorUtil.createStudent();
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findOne(em, Student.class).get(0);
        }
        Course course = lesson.getCourse();
        course.addStudent(student);
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
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc.perform(post("/api/v1/submissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId", Matchers.is(submission.getStudent().getId().intValue())))
                .andExpect(jsonPath("$.lessonId", Matchers.is(submission.getLesson().getId().intValue())))
                .andExpect(jsonPath("$.grade", Matchers.is(80.0)));

        // Validate new Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertEquals(submissionList.size(), databaseSizeBeforeCreate + 1);

        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getStudent()).isEqualTo(submission.getStudent());
        assertThat(testSubmission.getLesson()).isEqualTo(submission.getLesson());
        assertThat(testSubmission.getGrade()).isEqualTo(submission.getGrade());
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

        restSubmissionMockMvc.perform(
                post("/api/v1/lesson/" + lesson.getId() + "/student/" + student.getId() + "/submission")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId", Matchers.is(student.getId().intValue())))
                .andExpect(jsonPath("$.lessonId", Matchers.is(lesson.getId().intValue())))
                .andExpect(jsonPath("$.grade", Matchers.is(80.0)));

        // Validate new Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertEquals(submissionList.size(), databaseSizeBeforeCreate + 1);

        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getStudent()).isEqualTo(submission.getStudent());
        assertThat(testSubmission.getLesson()).isEqualTo(submission.getLesson());
        assertThat(testSubmission.getGrade()).isEqualTo(submission.getGrade());
    }

    @Test
    @Transactional
    @CustomMockInstructor
    void findByLessonWithInstructorRole() throws Exception {

        // Create submission
        Submission submission = createEntity(entityManager, 80.0);
        submissionRepository.save(submission);
        Lesson lesson = submission.getLesson();

        restSubmissionMockMvc.perform(
                get("/api/v1/lesson/" + lesson.getId() + "/submissions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
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

        restSubmissionMockMvc.perform(
                        delete("/api/v1/lesson/" + lesson.getId() + "/student/" + student.getId() + "/submission")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());

        List<Submission> submissions = submissionRepository.findAll();
        assertThat(submissions).isEmpty();
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
        TestUtil.equalsVerifier(SubmissionDTO.class);
        SubmissionDTO submissionDTO1 = new SubmissionDTO();
        submissionDTO1.setStudentId(1L);
        submissionDTO1.setLessonId(1L);
        SubmissionDTO submissionDTO2 = new SubmissionDTO();
        assertThat(submissionDTO1).isNotEqualTo(submissionDTO2);
        submissionDTO2.setStudentId(submissionDTO1.getStudentId());
        submissionDTO2.setLessonId(submissionDTO1.getLessonId());
        assertThat(submissionDTO1).isEqualTo(submissionDTO2);
        submissionDTO2.setStudentId(2L);
        submissionDTO2.setLessonId(2L);
        assertThat(submissionDTO1).isNotEqualTo(submissionDTO2);
        submissionDTO1.setStudentId(null);
        submissionDTO1.setLessonId(null);
        assertThat(submissionDTO1).isNotEqualTo(submissionDTO2);
    }
}