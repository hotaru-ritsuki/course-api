package com.example.courseapi.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.Submission;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.SubmissionDTO;
import com.example.courseapi.repository.*;
import com.example.courseapi.service.mapper.SubmissionMapper;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unused")
@ContextConfiguration(classes = {SubmissionServiceImpl.class})
@ExtendWith(SpringExtension.class)
class SubmissionServiceImplTest {
    @MockBean
    private SubmissionMapper submissionMapper;

    @MockBean
    private SubmissionRepository submissionRepository;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private InstructorRepository instructorRepository;

    @SpyBean
    private SubmissionServiceImpl submissionServiceImpl;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findById(Long, Long)}
     */
    @Test
    void testFindById() {
        Course course = new Course();
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("username");
        course.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("username");
        course.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("username");
        lesson.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("username");
        lesson.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");

        Student student = new Student();
        //student.setCourseFeedbacks(new HashSet<>());
        student.setCreatedBy("username");
        student.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        student.setEmail("user@courseapi.com");
        student.setFirstName("User");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("username");
        student.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        student.setPassword("iloveyou");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());

        Submission.SubmissionId submissionId = new Submission.SubmissionId();
        submissionId.setLessonId(1L);
        submissionId.setStudentId(1L);

        Submission submission = new Submission();
        submission.setCreatedBy("username");
        submission.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submission.setGrade(10.0d);
        submission.setLesson(lesson);
        submission.setModifiedBy("username");
        submission.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submission.setStudent(student);
        submission.setSubmissionId(submissionId);
        Optional<Submission> ofResult = Optional.of(submission);
        when(submissionRepository.findBySubmissionId_LessonIdAndSubmissionId_StudentId(Mockito.<Long>any(),
                Mockito.<Long>any())).thenReturn(ofResult);

        SubmissionDTO submissionDTO = new SubmissionDTO();
        submissionDTO.setCreatedBy("username");
        submissionDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submissionDTO.setGrade(10.0d);
        submissionDTO.setLessonId(1L);
        submissionDTO.setModifiedBy("username");
        submissionDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submissionDTO.setStudentId(1L);
        when(submissionMapper.toDto(Mockito.<Submission>any())).thenReturn(submissionDTO);
        assertTrue(submissionServiceImpl.findById(1L, 1L).isPresent());
        verify(submissionRepository).findBySubmissionId_LessonIdAndSubmissionId_StudentId(Mockito.<Long>any(),
                Mockito.<Long>any());
        verify(submissionMapper).toDto(Mockito.<Submission>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#save(SubmissionDTO, Long)}
     */
    @Test
    void testSave() {
        Course course = new Course();
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("username");
        course.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("username");
        course.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("username");
        lesson.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("username");
        lesson.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");

        Student student = new Student();
        //student.setCourseFeedbacks(new HashSet<>());
        student.setCreatedBy("username");
        student.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        student.setEmail("user@courseapi.com");
        student.setFirstName("User");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("username");
        student.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        student.setPassword("iloveyou");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());

        Submission.SubmissionId submissionId = new Submission.SubmissionId();
        submissionId.setLessonId(1L);
        submissionId.setStudentId(1L);

        Submission submission = new Submission();
        submission.setCreatedBy("username");
        submission.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submission.setGrade(10.0d);
        submission.setLesson(lesson);
        submission.setModifiedBy("username");
        submission.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submission.setStudent(student);
        submission.setSubmissionId(submissionId);
        when(submissionRepository.save(Mockito.<Submission>any())).thenReturn(submission);

        Course course2 = new Course();
        course2.setCourseFeedbacks(new HashSet<>());
        course2.setCreatedBy("username");
        course2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course2.setDescription("The characteristics of someone or something");
        course2.setId(1L);
        course2.setInstructors(new HashSet<>());
        course2.setLessons(new HashSet<>());
        course2.setModifiedBy("username");
        course2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course2.setStudents(new HashSet<>());
        course2.setTitle("Dr");

        Lesson lesson2 = new Lesson();
        lesson2.setCourse(course2);
        lesson2.setCreatedBy("username");
        lesson2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lesson2.setDescription("The characteristics of someone or something");
        lesson2.setId(1L);
        lesson2.setModifiedBy("username");
        lesson2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lesson2.setSubmissions(new HashSet<>());
        lesson2.setTitle("Dr");

        Student student2 = new Student();
//        student2.setCourseFeedbacks(new HashSet<>());
        student2.setCreatedBy("username");
        student2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        student2.setEmail("user@courseapi.com");
        student2.setFirstName("User");
        student2.setId(1L);
        student2.setLastName("LastName");
        student2.setModifiedBy("username");
        student2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        student2.setPassword("iloveyou");
        student2.setRole(Roles.ADMIN);
        student2.setStudentCourses(new HashSet<>());

        Submission.SubmissionId submissionId2 = new Submission.SubmissionId();
        submissionId2.setLessonId(1L);
        submissionId2.setStudentId(1L);

        Submission submission2 = new Submission();
        submission2.setCreatedBy("username");
        submission2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submission2.setGrade(10.0d);
        submission2.setLesson(lesson2);
        submission2.setModifiedBy("username");
        submission2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submission2.setStudent(student2);
        submission2.setSubmissionId(submissionId2);

        SubmissionDTO submissionDTO = new SubmissionDTO();
        submissionDTO.setCreatedBy("username");
        submissionDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submissionDTO.setGrade(10.0d);
        submissionDTO.setLessonId(1L);
        submissionDTO.setModifiedBy("username");
        submissionDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submissionDTO.setStudentId(1L);
        when(submissionMapper.toEntity(Mockito.<SubmissionDTO>any())).thenReturn(submission2);
        when(submissionMapper.toDto(Mockito.<Submission>any())).thenReturn(submissionDTO);

        SubmissionDTO submissionDTO2 = new SubmissionDTO();
        submissionDTO2.setCreatedBy("username");
        submissionDTO2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submissionDTO2.setGrade(10.0d);
        submissionDTO2.setLessonId(1L);
        submissionDTO2.setModifiedBy("username");
        submissionDTO2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submissionDTO2.setStudentId(1L);
        doNothing().when(submissionServiceImpl).validateSubmission(Mockito.<Long>any(),Mockito.<Long>any(),Mockito.<Long>any());
        assertSame(submissionDTO, submissionServiceImpl.save(submissionDTO2, 1L));
        verify(submissionRepository).save(Mockito.<Submission>any());
        verify(submissionMapper).toEntity(Mockito.<SubmissionDTO>any());
        verify(submissionMapper).toDto(Mockito.<Submission>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findAll()}
     */
    @Test
    void testFindAll() {
        when(submissionRepository.findAll()).thenReturn(new ArrayList<>());
        ArrayList<SubmissionDTO> submissionDTOList = new ArrayList<>();
        when(submissionMapper.toDto(Mockito.<List<Submission>>any())).thenReturn(submissionDTOList);
        List<SubmissionDTO> actualFindAllResult = submissionServiceImpl.findAll();
        assertSame(submissionDTOList, actualFindAllResult);
        assertTrue(actualFindAllResult.isEmpty());
        verify(submissionRepository).findAll();
        verify(submissionMapper).toDto(Mockito.<List<Submission>>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#delete(Long, Long)}
     */
    @Test
    void testDelete() {
        doNothing().when(submissionRepository)
                .deleteBySubmissionId_LessonIdAndSubmissionId_StudentId(Mockito.<Long>any(), Mockito.<Long>any());
        submissionServiceImpl.delete(1L, 1L);
        verify(submissionRepository).deleteBySubmissionId_LessonIdAndSubmissionId_StudentId(Mockito.<Long>any(),
                Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findAllByStudentIdAndCourseId(Long, Long)}
     */
    @Test
    void testFindAllByStudentIdAndCourseId() {
        ArrayList<Submission> submissionList = new ArrayList<>();
        when(submissionRepository.findAllByStudentIdAndLessonCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(submissionList);
        List<Submission> actualFindAllByStudentIdAndCourseIdResult = submissionServiceImpl
                .findAllByStudentIdAndCourseId(1L, 1L);
        assertSame(submissionList, actualFindAllByStudentIdAndCourseIdResult);
        assertTrue(actualFindAllByStudentIdAndCourseIdResult.isEmpty());
        verify(submissionRepository).findAllByStudentIdAndLessonCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }
}

