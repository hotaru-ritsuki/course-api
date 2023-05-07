package com.example.courseapi.e2e;

import com.example.courseapi.config.MockMvcBuilderTestConfiguration;
import com.example.courseapi.config.PostgresTestContainer;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.*;
import com.example.courseapi.domain.enums.CourseStatus;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.request.*;
import com.example.courseapi.dto.response.*;
import com.example.courseapi.rest.*;
import com.example.courseapi.security.config.SecurityConfig;
import com.example.courseapi.security.controller.AuthController;
import com.example.courseapi.security.dto.JWTRefreshDTO;
import com.example.courseapi.security.dto.JWTTokenDTO;
import com.example.courseapi.security.dto.LoginRequestDTO;
import com.example.courseapi.security.dto.SignUpRequestDTO;
import com.example.courseapi.service.mapper.LessonMapper;
import com.example.courseapi.util.EntityCreatorUtil;
import com.example.courseapi.util.JacksonUtil;
import com.example.courseapi.util.ResponsePage;
import com.example.courseapi.util.TestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Scenarios:
 * 1. Registration, Authentication and Role Management
 * 2. Course Management
 * 3. Homework and Grading
 */
@DefaultTestConfiguration
@SpringBootTest
@Import(SecurityConfig.class)
class CourseManagementScenariosTest extends PostgresTestContainer {

    @Autowired
    private MockMvcBuilderTestConfiguration mockMvcBuilderTestConfiguration;

    @Autowired
    private CourseController courseController;
    @Autowired
    private CourseFeedbackController courseFeedbackController;
    @Autowired
    private HomeworkController homeworkController;
    @Autowired
    private LessonController lessonController;
    @Autowired
    private SubmissionController submissionController;
    @Autowired
    private UserController userController;
    @Autowired
    private AuthController authController;
    @Autowired
    private LessonMapper lessonMapper;

    private MockMvc mockMvc;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
        this.mockMvc = mockMvcBuilderTestConfiguration.forSecurityControllers(
                courseController, courseFeedbackController, homeworkController,
                lessonController, submissionController, userController, authController
        );
    }

    @Test
    void scenario1_userRegistrationAndLogin() throws Exception {
        // Create detached entity
        User user = EntityCreatorUtil.createUser(RandomStringUtils.randomAlphabetic(10));

        // Register user
        signUpUserStep(user);

        // Login registered user
        JWTTokenDTO jwtTokenDTO = loginUserStep(user);

        // Attempt request and expect unauthenticated
        attemptUnauthorizedRequestStep();

        // Check if user can authenticate with access token
        attemptAuthenticatedRequestStep(user, jwtTokenDTO);

        // Assume access token is expired let's try to get new access token by refresh
        refreshAccessTokenAndValidateStep(user, jwtTokenDTO);

        // Delete user step
        deleteUserStep(user);
    }

    @Test
    void scenario2_courseManagement() throws Exception {
        // Admin
        Admin admin = EntityCreatorUtil.predefinedAdmin();
        JWTTokenDTO jwtAdminDTO = loginUserStep(admin);

        // Student
        Student student = EntityCreatorUtil.createStudent(RandomStringUtils.randomAlphabetic(10));
        signUpUserStep(student);

        JWTTokenDTO jwtStudentDTO = loginUserStep(student);
        UserResponseDTO studentResponseDTO = attemptAuthenticatedRequestStep(student, jwtStudentDTO);

        // Assign STUDENT role
        assignRolesStep(jwtAdminDTO, studentResponseDTO, Roles.STUDENT);

        // Instructor
        Instructor instructor = EntityCreatorUtil.createInstructor(RandomStringUtils.randomAlphabetic(10));
        signUpUserStep(instructor);
        JWTTokenDTO jwtInstructorDTO = loginUserStep(instructor);
        UserResponseDTO instructorResponseDTO = attemptAuthenticatedRequestStep(instructor, jwtInstructorDTO);

        // Assign INSTRUCTOR role
        assignRolesStep(jwtAdminDTO, instructorResponseDTO, Roles.INSTRUCTOR);

        // Create course
        CourseResponseDTO courseDTO = createCourseStep(instructorResponseDTO.getId(), jwtAdminDTO);

        // Create lessons
        uploadAndUpdateLessonsForCourseStep(courseDTO, jwtAdminDTO);

        // Subscribe student
        subscribeStudentStep(courseDTO, jwtStudentDTO);
        validateStudentSubscribedStep(courseDTO, studentResponseDTO, jwtAdminDTO);

        // Remove student and instructor
        deleteUserStep(student);
        deleteUserStep(instructor);

        // Remove courses
        deleteCourseStep(courseDTO);
    }

    @Test
    void scenario3_homeworkAndGrading() throws Exception {
        // Admin
        Admin admin = EntityCreatorUtil.predefinedAdmin();
        JWTTokenDTO jwtAdminDTO = loginUserStep(admin);

        // Student 1
        Student student1 = EntityCreatorUtil.createStudent(RandomStringUtils.randomAlphabetic(10));
        signUpUserStep(student1);
        JWTTokenDTO jwtStudent1DTO = loginUserStep(student1);
        UserResponseDTO student1ResponseDTO = attemptAuthenticatedRequestStep(student1, jwtStudent1DTO);
        // Assign STUDENT role
        assignRolesStep(jwtAdminDTO, student1ResponseDTO, Roles.STUDENT);

        // Student 1
        Student student2 = EntityCreatorUtil.createStudent(RandomStringUtils.randomAlphabetic(10));
        signUpUserStep(student2);
        JWTTokenDTO jwtStudent2DTO = loginUserStep(student2);
        UserResponseDTO student2ResponseDTO = attemptAuthenticatedRequestStep(student2, jwtStudent2DTO);
        // Assign STUDENT role
        assignRolesStep(jwtAdminDTO, student2ResponseDTO, Roles.STUDENT);

        // Instructor
        Instructor instructor = EntityCreatorUtil.createInstructor(RandomStringUtils.randomAlphabetic(10));
        signUpUserStep(instructor);
        JWTTokenDTO jwtInstructorDTO = loginUserStep(instructor);
        UserResponseDTO instructorResponseDTO = attemptAuthenticatedRequestStep(instructor, jwtInstructorDTO);

        // Assign INSTRUCTOR role
        assignRolesStep(jwtAdminDTO, instructorResponseDTO, Roles.INSTRUCTOR);

        // Create course
        CourseResponseDTO courseDTO = createCourseStep(instructorResponseDTO.getId(), jwtAdminDTO);

        // Create lessons
        LessonsUpdateDTO lessonsUpdateDTO = prepareLessonsStep();

        // Upload lessons
        CourseResponseDTO courseLessonsDTO = uploadLessonsStep(courseDTO.getId(), lessonsUpdateDTO, jwtInstructorDTO);

        // Subscribe student 1
        subscribeStudentStep(courseDTO, jwtStudent1DTO);
        validateStudentSubscribedStep(courseDTO, student1ResponseDTO, jwtAdminDTO);

        // Subscribe student 2
        subscribeStudentStep(courseDTO, jwtStudent2DTO);
        validateStudentSubscribedStep(courseDTO, student2ResponseDTO, jwtAdminDTO);

        // Upload homeworks for lessons
        uploadLessonHomeworkStep(courseLessonsDTO, jwtStudent1DTO);
        Page<HomeworkResponseDTO> homeworkResponseDTOS = validateHomeworksStep(jwtStudent1DTO, jwtInstructorDTO);

        // Assign submissions for lessons
        assignSubmissionsCourseSuccessfulStep(courseLessonsDTO, student1ResponseDTO.getId(), jwtStudent1DTO, jwtInstructorDTO);
        assignSubmissionsCourseFailedStep(courseLessonsDTO, student2ResponseDTO.getId(), jwtStudent2DTO, jwtInstructorDTO);

        // Leave course feedback
        saveCourseFeedbackStep(courseLessonsDTO,  student1ResponseDTO.getId(), jwtStudent1DTO);
    }

    void saveCourseFeedbackStep(CourseResponseDTO courseLessonsDTO, Long studentId, JWTTokenDTO jwtStudent1DTO)
            throws Exception {
        CourseFeedbackRequestDTO courseFeedbackRequestDTO = new CourseFeedbackRequestDTO();
        courseFeedbackRequestDTO.setCourseId(courseLessonsDTO.getId());
        courseFeedbackRequestDTO.setFeedback(RandomStringUtils.randomAlphabetic(50));
        MvcResult courseFeedbackMvcResult = mockMvc.perform(post("/api/v1/course-feedbacks")
                        .headers(getHttpHeadersFromToken(jwtStudent1DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseFeedbackRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        CourseFeedbackResponseDTO courseFeedbackResponseDTO = JacksonUtil.deserialize(courseFeedbackMvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        mockMvc.perform(get("/api/v1/course-feedbacks/" + courseFeedbackResponseDTO.getId())
                        .headers(getHttpHeadersFromToken(jwtStudent1DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    void assignSubmissionsCourseFailedStep(CourseResponseDTO courseDTO, Long studentId,
                                               JWTTokenDTO jwtStudentDTO, JWTTokenDTO jwtInstructorDTO) throws Exception {
        Set<LessonResponseDTO> lessons = courseDTO.getLessons();

        // Put submissions for first 3 lessons
        Iterable<LessonResponseDTO> first3LessonResponseDTOS = Iterables.limit(lessons, 3);
        for (LessonResponseDTO lessonResponseDTO: first3LessonResponseDTOS) {
            SubmissionRequestDTO submissionRequestDTO = new SubmissionRequestDTO();
            submissionRequestDTO.setGrade(new Random().nextDouble(0.0, 80.0));
            submissionRequestDTO.setLessonId(lessonResponseDTO.getId());
            submissionRequestDTO.setStudentId(studentId);
            mockMvc.perform(post("/api/v1/submissions")
                            .headers(getHttpHeadersFromToken(jwtInstructorDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtil.convertObjectToJsonBytes(submissionRequestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.grade", Matchers.is(submissionRequestDTO.getGrade())))
                    .andExpect(jsonPath("$.lessonId", Matchers.is(submissionRequestDTO.getLessonId().intValue())))
                    .andExpect(jsonPath("$.studentId", Matchers.is(submissionRequestDTO.getStudentId().intValue())));
        }

        mockMvc.perform(get("/api/v1/courses/" + courseDTO.getId() + "/status")
                        .headers(getHttpHeadersFromToken(jwtStudentDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseStatus", Matchers.is(CourseStatus.IN_PROGRESS.name())))
                .andExpect(jsonPath("$.finalGrade", Matchers.nullValue()));

        mockMvc.perform(get("/api/v1/courses/my")
                        .headers(getHttpHeadersFromToken(jwtStudentDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.*.courseStatus", Matchers.hasItem(CourseStatus.IN_PROGRESS.name())))
                .andExpect(jsonPath("$.*.finalGrade", Matchers.hasItem(Matchers.nullValue())));


        Iterable<LessonResponseDTO> last2LessonResponseDTOS = Iterables.limit(Iterables.skip(lessons, 3), 2);
        for (LessonResponseDTO lessonResponseDTO: last2LessonResponseDTOS) {
            SubmissionRequestDTO submissionRequestDTO = new SubmissionRequestDTO();
            submissionRequestDTO.setGrade(new Random().nextDouble(80.0, 100.0));
            submissionRequestDTO.setLessonId(lessonResponseDTO.getId());
            submissionRequestDTO.setStudentId(studentId);
            mockMvc.perform(post("/api/v1/submissions")
                            .headers(getHttpHeadersFromToken(jwtInstructorDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtil.convertObjectToJsonBytes(submissionRequestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.grade", Matchers.is(submissionRequestDTO.getGrade())))
                    .andExpect(jsonPath("$.lessonId", Matchers.is(submissionRequestDTO.getLessonId().intValue())))
                    .andExpect(jsonPath("$.studentId", Matchers.is(submissionRequestDTO.getStudentId().intValue())));
        }

        mockMvc.perform(get("/api/v1/courses/" + courseDTO.getId() + "/status")
                        .headers(getHttpHeadersFromToken(jwtStudentDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseStatus", Matchers.is(CourseStatus.FAILED.name())))
                .andExpect(jsonPath("$.finalGrade", Matchers.notNullValue()));

        mockMvc.perform(get("/api/v1/courses/my")
                        .headers(getHttpHeadersFromToken(jwtStudentDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.*.courseStatus", Matchers.hasItem(CourseStatus.FAILED.name())))
                .andExpect(jsonPath("$.*.finalGrade", Matchers.hasItem(Matchers.notNullValue())));
    }

    void assignSubmissionsCourseSuccessfulStep(CourseResponseDTO courseDTO, Long studentId,
                                               JWTTokenDTO jwtStudentDTO, JWTTokenDTO jwtInstructorDTO) throws Exception {
        Set<LessonResponseDTO> lessons = courseDTO.getLessons();

        // Put submissions for first 3 lessons
        Iterable<LessonResponseDTO> first3LessonResponseDTOS = Iterables.limit(lessons, 3);
        for (LessonResponseDTO lessonResponseDTO: first3LessonResponseDTOS) {
            SubmissionRequestDTO submissionRequestDTO = new SubmissionRequestDTO();
            submissionRequestDTO.setGrade(new Random().nextDouble(80.0, 100.0));
            submissionRequestDTO.setLessonId(lessonResponseDTO.getId());
            submissionRequestDTO.setStudentId(studentId);
            mockMvc.perform(post("/api/v1/submissions")
                            .headers(getHttpHeadersFromToken(jwtInstructorDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtil.convertObjectToJsonBytes(submissionRequestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.grade", Matchers.is(submissionRequestDTO.getGrade())))
                    .andExpect(jsonPath("$.lessonId", Matchers.is(submissionRequestDTO.getLessonId().intValue())))
                    .andExpect(jsonPath("$.studentId", Matchers.is(submissionRequestDTO.getStudentId().intValue())));
        }

        mockMvc.perform(get("/api/v1/courses/" + courseDTO.getId() + "/status")
                        .headers(getHttpHeadersFromToken(jwtStudentDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseStatus", Matchers.is(CourseStatus.IN_PROGRESS.name())))
                .andExpect(jsonPath("$.finalGrade", Matchers.nullValue()));

        mockMvc.perform(get("/api/v1/courses/my")
                        .headers(getHttpHeadersFromToken(jwtStudentDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.*.courseStatus", Matchers.hasItem(CourseStatus.IN_PROGRESS.name())))
                .andExpect(jsonPath("$.*.finalGrade", Matchers.hasItem(Matchers.nullValue())));

        Iterable<LessonResponseDTO> last2LessonResponseDTOS = Iterables.limit(Iterables.skip(lessons, 3), 2);
        for (LessonResponseDTO lessonResponseDTO: last2LessonResponseDTOS) {
            SubmissionRequestDTO submissionRequestDTO = new SubmissionRequestDTO();
            submissionRequestDTO.setGrade(new Random().nextDouble(80.0, 100.0));
            submissionRequestDTO.setLessonId(lessonResponseDTO.getId());
            submissionRequestDTO.setStudentId(studentId);
            mockMvc.perform(post("/api/v1/submissions")
                            .headers(getHttpHeadersFromToken(jwtInstructorDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtil.convertObjectToJsonBytes(submissionRequestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.grade", Matchers.is(submissionRequestDTO.getGrade())))
                    .andExpect(jsonPath("$.lessonId", Matchers.is(submissionRequestDTO.getLessonId().intValue())))
                    .andExpect(jsonPath("$.studentId", Matchers.is(submissionRequestDTO.getStudentId().intValue())));
        }

        mockMvc.perform(get("/api/v1/courses/" + courseDTO.getId() + "/status")
                        .headers(getHttpHeadersFromToken(jwtStudentDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseStatus", Matchers.is(CourseStatus.COMPLETED.name())))
                .andExpect(jsonPath("$.finalGrade", Matchers.notNullValue()));

        mockMvc.perform(get("/api/v1/courses/my")
                        .headers(getHttpHeadersFromToken(jwtStudentDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.*.courseStatus", Matchers.hasItem(CourseStatus.COMPLETED.name())))
                .andExpect(jsonPath("$.*.finalGrade", Matchers.hasItem(Matchers.notNullValue())));
    }

    Page<HomeworkResponseDTO> validateHomeworksStep(JWTTokenDTO jwtStudentDTO, JWTTokenDTO jwtInstructorDTO) throws Exception {
        // Get homeworks from instructor
        mockMvc.perform(get("/api/v1/homeworks?sort=id,desc")
                        .headers(getHttpHeadersFromToken(jwtInstructorDTO)))
                .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content").exists())
                        .andExpect(jsonPath("$.content").isArray())
                        .andExpect(jsonPath("$.content", Matchers.hasSize(5)))
                        .andExpect(jsonPath("$.totalElements", Matchers.is(5)))
                        .andExpect(jsonPath("$.numberOfElements", Matchers.is(5)))
                        .andExpect(jsonPath("$.sort.sorted", Matchers.is(true)));

        // Get homeworks from student
        MvcResult studentHomeworksResponse = mockMvc.perform(get("/api/v1/homeworks?sort=id,desc")
                        .headers(getHttpHeadersFromToken(jwtStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(5)))
                .andExpect(jsonPath("$.totalElements", Matchers.is(5)))
                .andExpect(jsonPath("$.numberOfElements", Matchers.is(5)))
                .andExpect(jsonPath("$.sort.sorted", Matchers.is(true)))
                .andReturn();

        return JacksonUtil.deserialize(studentHomeworksResponse.getResponse().getContentAsString(),
                new TypeReference<ResponsePage<HomeworkResponseDTO>>() {});
    }

    void uploadLessonHomeworkStep(CourseResponseDTO courseLessonsDTO, JWTTokenDTO jwtStudentDTO) throws Exception {
        for (LessonResponseDTO lesson : courseLessonsDTO.getLessons()) {
            mockMvc.perform(multipart("/api/v1/lesson/" + lesson.getId() + "/homework")
                            .file("file", ("{\"key" + lesson.getCourseId() + "\": \"value1\"}").getBytes())
                            .headers(getHttpHeadersFromToken(jwtStudentDTO)))
                    .andExpect(status().isCreated());
        }
    }

    LessonsUpdateDTO prepareLessonsStep() {
        LessonRequestDTO lesson1DTO = LessonRequestDTO.builder()
                .title("Lesson 1 Title")
                .description("Lesson 1 Description").build();
        LessonRequestDTO lesson2DTO = LessonRequestDTO.builder()
                .title("Lesson 2 Title")
                .description("Lesson 2 Description").build();
        LessonRequestDTO lesson3DTO = LessonRequestDTO.builder()
                .title("Lesson 3 Title")
                .description("Lesson 3 Description").build();
        LessonRequestDTO lesson4DTO = LessonRequestDTO.builder()
                .title("Lesson 4 Title")
                .description("Lesson 4 Description").build();
        LessonRequestDTO lesson5DTO = LessonRequestDTO.builder()
                .title("Lesson 5 Title")
                .description("Lesson 5 Description").build();
        return new LessonsUpdateDTO(Set.of(lesson1DTO, lesson2DTO, lesson3DTO, lesson4DTO, lesson5DTO));
    }

    CourseResponseDTO uploadLessonsStep(Long courseId, LessonsUpdateDTO lessonsUpdateDTO, JWTTokenDTO jwtAdminDTO) throws Exception {
        final MvcResult lessonsMvcResult = mockMvc.perform(put("/api/v1/courses/" + courseId + "/lessons")
                        .headers(getHttpHeadersFromToken(jwtAdminDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(lessonsUpdateDTO)))
                .andExpect(status().isOk())
                .andReturn();

        return JacksonUtil.deserialize(lessonsMvcResult.getResponse().getContentAsString(),
                new TypeReference<CourseResponseDTO>() {});
    }

    void uploadAndUpdateLessonsForCourseStep(CourseResponseDTO courseDTO, JWTTokenDTO jwtAdminDTO) throws Exception {
        // Create lessons
        LessonsUpdateDTO lessonsUpdateDTO = prepareLessonsStep();

        // Upload lessons
        CourseResponseDTO courseLessonsDTO = uploadLessonsStep(courseDTO.getId(), lessonsUpdateDTO, jwtAdminDTO);

        // Update course lessons
        Set<LessonRequestDTO> lessonsToPatch = new HashSet<>(lessonMapper.fromResponseToRequest(courseLessonsDTO.getLessons()));

        //noinspection OptionalGetWithoutIsPresent
        LessonRequestDTO lesson1DTOToUpdate = lessonsToPatch.stream()
                .filter(lesson -> lesson.getTitle().equals("Lesson 1 Title")).findFirst().get();
        lesson1DTOToUpdate.setTitle("Lesson 1 Title Updated");
        lesson1DTOToUpdate.setDescription("Lesson 1 Description Updated");

        //noinspection OptionalGetWithoutIsPresent
        LessonRequestDTO lesson4DTOToDelete = lessonsToPatch.stream()
                .filter(lesson -> lesson.getTitle().equals("Lesson 4 Title")).findFirst().get();
        lessonsToPatch.remove(lesson4DTOToDelete);

        LessonRequestDTO lesson6DTO = LessonRequestDTO.builder()
                .title("Lesson 5 Title")
                .description("Lesson 5 Description").build();
        lessonsToPatch.add(lesson6DTO);

        LessonsUpdateDTO lessonsPatchDTO = new LessonsUpdateDTO(lessonsToPatch);

        CourseResponseDTO courseLessonsPatchedResponseDTO = uploadLessonsStep(courseDTO.getId(), lessonsPatchDTO, jwtAdminDTO);

        // Verify lessons update
        Optional<LessonResponseDTO> lesson4DTOBeforeOpt = courseLessonsDTO.getLessons().stream()
                .filter(lesson -> lesson.getTitle().equals("Lesson 4 Title")).findFirst();
        assertThat(lesson4DTOBeforeOpt).isPresent();

        Optional<LessonResponseDTO> lesson4DTOAfterOpt = courseLessonsPatchedResponseDTO.getLessons().stream()
                .filter(lesson -> lesson.getTitle().equals("Lesson 4 Title")).findFirst();
        assertThat(lesson4DTOAfterOpt).isNotPresent();

        Optional<LessonResponseDTO> lesson1DTOUpdatedOpt = courseLessonsPatchedResponseDTO.getLessons().stream()
                .filter(lesson -> lesson.getTitle().equals("Lesson 1 Title Updated")).findFirst();
        assertThat(lesson1DTOUpdatedOpt).isPresent();
    }

    CourseResponseDTO createCourseStep(Long instructorId, JWTTokenDTO jwtAdminDTO) throws Exception {
        CourseRequestDTO courseRequestDTO = new CourseRequestDTO();
        courseRequestDTO.setTitle("Course Title");
        courseRequestDTO.setDescription("Course Description");
        courseRequestDTO.setInstructorIds(Set.of(instructorId));
        final MvcResult courseMvcResult = mockMvc.perform(post("/api/v1/courses")
                        .headers(getHttpHeadersFromToken(jwtAdminDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(courseRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        return JacksonUtil.deserialize(courseMvcResult.getResponse().getContentAsString(),
                new TypeReference<CourseResponseDTO>() {});
    }

    void deleteCourseStep(CourseResponseDTO courseDTO) throws Exception {
        // Admin
        Admin admin = EntityCreatorUtil.predefinedAdmin();
        JWTTokenDTO jwtAdminDTO = loginUserStep(admin);

        // Delete user
        mockMvc.perform(delete("/api/v1/courses/" + courseDTO.getId())
                        .headers(getHttpHeadersFromToken(jwtAdminDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    void deleteUserStep(User user) throws Exception {
        // Admin
        Admin admin = EntityCreatorUtil.predefinedAdmin();
        JWTTokenDTO jwtAdminDTO = loginUserStep(admin);

        // Get user id
        JWTTokenDTO jwtUserDTO = loginUserStep(user);
        UserResponseDTO userDTO = attemptAuthenticatedRequestStep(user, jwtUserDTO);

        // Delete user
        mockMvc.perform(delete("/api/v1/users/" + userDTO.getId())
                        .headers(getHttpHeadersFromToken(jwtAdminDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    void subscribeStudentStep(CourseResponseDTO courseDTO, JWTTokenDTO jwtStudentDTO) throws Exception {
        mockMvc.perform(put("/api/v1/courses/" + courseDTO.getId() + "/subscribe")
                        .headers(getHttpHeadersFromToken(jwtStudentDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    void validateStudentSubscribedStep(CourseResponseDTO course, UserResponseDTO student, JWTTokenDTO jwtAdminDTO) throws Exception {
        final MvcResult actualCourseResult = mockMvc.perform(get("/api/v1/courses/" + course.getId())
                .headers(getHttpHeadersFromToken(jwtAdminDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final CourseResponseDTO actualCourseDTO = JacksonUtil.deserialize(actualCourseResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertThat(actualCourseDTO.getStudentIds()).contains(student.getId());
    }

    void assignRolesStep(JWTTokenDTO jwtAdminDTO, UserResponseDTO userDTO, Roles role) throws Exception {
        RoleRequestDTO roleRequestDTO = new RoleRequestDTO(role);

        mockMvc.perform(post("/api/v1/users/" + userDTO.getId() + "/role")
                        .headers(getHttpHeadersFromToken(jwtAdminDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(roleRequestDTO)))
                .andExpect(status().isOk());
    }

    void signUpUserStep(User user) throws Exception {
        final SignUpRequestDTO signUpRequestDTO = SignUpRequestDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password("password")
                .build();

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(signUpRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();
    }

    JWTTokenDTO loginUserStep(final User user) throws Exception {
        final LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .email(user.getEmail())
                .password("password")
                .build();

        final MvcResult loginMvcResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(loginRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();

        return JacksonUtil.deserialize(loginMvcResult.getResponse().getContentAsString(),
                new TypeReference<JWTTokenDTO>() {});
    }

    private void attemptUnauthorizedRequestStep() throws Exception {
        mockMvc.perform(get("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    UserResponseDTO attemptAuthenticatedRequestStep(final User user, final JWTTokenDTO jwtTokenDTO) throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v1/users/me").headers(getHttpHeadersFromToken(jwtTokenDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDTO userDTO = JacksonUtil.deserialize(mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertThat(userDTO.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDTO.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDTO.getLastName()).isEqualTo(user.getLastName());
        return userDTO;
    }

    private static HttpHeaders getHttpHeadersFromToken(JWTTokenDTO jwtTokenDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenDTO.getAccessToken());
        return headers;
    }

    private void refreshAccessTokenAndValidateStep(final User user, final JWTTokenDTO jwtTokenDTO) throws Exception {
        JWTRefreshDTO jwtRefreshDTO = JWTRefreshDTO.builder()
                .refreshToken(jwtTokenDTO.getRefreshToken())
                .build();

        MvcResult jwtRefreshMvcResult = mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(jwtRefreshDTO)))
                .andExpect(status().isOk())
                .andReturn();

        JWTTokenDTO refreshedJwtTokenDTO = JacksonUtil.deserialize(jwtRefreshMvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        MvcResult mvcRefreshedTokensResult = mockMvc.perform(
                        get("/api/v1/users/me").headers(getHttpHeadersFromToken(refreshedJwtTokenDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDTO userRefreshedResponseDTO = JacksonUtil.deserialize(mvcRefreshedTokensResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertThat(userRefreshedResponseDTO.getEmail()).isEqualTo(user.getEmail());
        assertThat(userRefreshedResponseDTO.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userRefreshedResponseDTO.getLastName()).isEqualTo(user.getLastName());
    }
}