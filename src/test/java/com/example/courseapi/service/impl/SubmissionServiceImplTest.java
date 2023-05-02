package com.example.courseapi.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.courseapi.domain.Admin;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Instructor;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.Submission;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.request.SubmissionRequestDTO;
import com.example.courseapi.dto.response.SubmissionResponseDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.repository.SubmissionRepository;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.mapper.SubmissionMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SubmissionServiceImpl.class})
@ExtendWith(SpringExtension.class)
class SubmissionServiceImplTest {
    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private SubmissionMapper submissionMapper;

    @MockBean
    private SubmissionRepository submissionRepository;

    @Autowired
    private SubmissionServiceImpl submissionServiceImpl;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link SubmissionServiceImpl#findById(Long, Long)}
     */
    @Test
    void testFindById() {
        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");

        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());

        Submission.SubmissionId submissionId = new Submission.SubmissionId();
        submissionId.setLessonId(1L);
        submissionId.setStudentId(1L);

        Submission submission = new Submission();
        submission.setCreatedBy("Anonymous");
        submission.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission.setGrade(10.0d);
        submission.setLesson(lesson);
        submission.setModifiedBy("Anonymous");
        submission.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission.setStudent(student);
        submission.setSubmissionId(submissionId);
        Optional<Submission> ofResult = Optional.of(submission);
        when(submissionRepository.findBySubmissionId_LessonIdAndSubmissionId_StudentId(Mockito.<Long>any(),
                Mockito.<Long>any())).thenReturn(ofResult);
        when(submissionMapper.toResponseDto(Mockito.<Submission>any())).thenReturn(new SubmissionResponseDTO());
        assertTrue(submissionServiceImpl.findById(1L, 1L).isPresent());
        verify(submissionRepository).findBySubmissionId_LessonIdAndSubmissionId_StudentId(Mockito.<Long>any(),
                Mockito.<Long>any());
        verify(submissionMapper).toResponseDto(Mockito.<Submission>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findById(Long, Long)}
     */
    @Test
    void testFindById2() {

        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");

        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());

        Submission.SubmissionId submissionId = new Submission.SubmissionId();
        submissionId.setLessonId(1L);
        submissionId.setStudentId(1L);

        Submission submission = new Submission();
        submission.setCreatedBy("Anonymous");
        submission.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission.setGrade(10.0d);
        submission.setLesson(lesson);
        submission.setModifiedBy("Anonymous");
        submission.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission.setStudent(student);
        submission.setSubmissionId(submissionId);
        Optional<Submission> ofResult = Optional.of(submission);
        when(submissionRepository.findBySubmissionId_LessonIdAndSubmissionId_StudentId(Mockito.<Long>any(),
                Mockito.<Long>any())).thenReturn(ofResult);
        submissionServiceImpl.findById(1L, 1L);
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#save(SubmissionRequestDTO)}
     */
    @Test
    void testSave() {
        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");
        Optional<Lesson> ofResult = Optional.of(lesson);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());
        Optional<Student> ofResult2 = Optional.of(student);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(SystemException.class, () -> submissionServiceImpl.save(new SubmissionRequestDTO()));
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(studentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#save(SubmissionRequestDTO)}
     */
    @Test
    void testSave2() {
        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");
        Optional<Lesson> ofResult = Optional.of(lesson);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(studentRepository.findById(Mockito.<Long>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> submissionServiceImpl.save(new SubmissionRequestDTO()));
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(studentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#save(SubmissionRequestDTO)}
     */
    @Test
    void testSave3() {
        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");

        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());

        Submission.SubmissionId submissionId = new Submission.SubmissionId();
        submissionId.setLessonId(1L);
        submissionId.setStudentId(1L);

        Submission submission = new Submission();
        submission.setCreatedBy("Anonymous");
        submission.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission.setGrade(10.0d);
        submission.setLesson(lesson);
        submission.setModifiedBy("Anonymous");
        submission.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission.setStudent(student);
        submission.setSubmissionId(submissionId);
        when(submissionRepository.save(Mockito.<Submission>any())).thenReturn(submission);

        Course course2 = new Course();
        course2.setAvailable(true);
        course2.setCourseFeedbacks(new HashSet<>());
        course2.setCreatedBy("Anonymous");
        course2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course2.setDescription("The characteristics of someone or something");
        course2.setId(1L);
        course2.setInstructors(new HashSet<>());
        course2.setLessons(new HashSet<>());
        course2.setModifiedBy("Anonymous");
        course2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course2.setStudents(new HashSet<>());
        course2.setTitle("Dr");

        Student student2 = new Student();
        student2.setCreatedBy("Anonymous");
        student2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student2.setEmail("boom.boom@courseapi.org");
        student2.setFirstName("FirstName");
        student2.setId(1L);
        student2.setLastName("LastName");
        student2.setModifiedBy("Anonymous");
        student2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student2.setPassword("SuperSecuredPassword");
        student2.setRole(Roles.ADMIN);
        student2.setStudentCourses(new HashSet<>());

        HashSet<Student> students = new HashSet<>();
        students.add(student2);

        Course course3 = new Course();
        course3.setAvailable(true);
        course3.setCourseFeedbacks(new HashSet<>());
        course3.setCreatedBy("Anonymous");
        course3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course3.setDescription("The characteristics of someone or something");
        course3.setId(1L);
        course3.setInstructors(new HashSet<>());
        course3.setLessons(new HashSet<>());
        course3.setModifiedBy("Anonymous");
        course3.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course3.setStudents(students);
        course3.setTitle("Dr");
        Lesson lesson2 = mock(Lesson.class);
        when(lesson2.getCourse()).thenReturn(course3);
        doNothing().when(lesson2).setCreatedBy(Mockito.<String>any());
        doNothing().when(lesson2).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(lesson2).setModifiedBy(Mockito.<String>any());
        doNothing().when(lesson2).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(lesson2).setCourse(Mockito.<Course>any());
        doNothing().when(lesson2).setDescription(Mockito.<String>any());
        doNothing().when(lesson2).setId(Mockito.<Long>any());
        doNothing().when(lesson2).setSubmissions(Mockito.<Set<Submission>>any());
        doNothing().when(lesson2).setTitle(Mockito.<String>any());
        lesson2.setCourse(course2);
        lesson2.setCreatedBy("Anonymous");
        lesson2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setDescription("The characteristics of someone or something");
        lesson2.setId(1L);
        lesson2.setModifiedBy("Anonymous");
        lesson2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setSubmissions(new HashSet<>());
        lesson2.setTitle("Dr");
        Optional<Lesson> ofResult = Optional.of(lesson2);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Student student3 = new Student();
        student3.setCreatedBy("Anonymous");
        student3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student3.setEmail("boom.boom@courseapi.org");
        student3.setFirstName("FirstName");
        student3.setId(1L);
        student3.setLastName("LastName");
        student3.setModifiedBy("Anonymous");
        student3.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student3.setPassword("SuperSecuredPassword");
        student3.setRole(Roles.ADMIN);
        student3.setStudentCourses(new HashSet<>());
        Optional<Student> ofResult2 = Optional.of(student3);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        Course course4 = new Course();
        course4.setAvailable(true);
        course4.setCourseFeedbacks(new HashSet<>());
        course4.setCreatedBy("Anonymous");
        course4.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course4.setDescription("The characteristics of someone or something");
        course4.setId(1L);
        course4.setInstructors(new HashSet<>());
        course4.setLessons(new HashSet<>());
        course4.setModifiedBy("Anonymous");
        course4.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course4.setStudents(new HashSet<>());
        course4.setTitle("Dr");

        Lesson lesson3 = new Lesson();
        lesson3.setCourse(course4);
        lesson3.setCreatedBy("Anonymous");
        lesson3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson3.setDescription("The characteristics of someone or something");
        lesson3.setId(1L);
        lesson3.setModifiedBy("Anonymous");
        lesson3.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson3.setSubmissions(new HashSet<>());
        lesson3.setTitle("Dr");

        Student student4 = new Student();
        student4.setCreatedBy("Anonymous");
        student4.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student4.setEmail("boom.boom@courseapi.org");
        student4.setFirstName("FirstName");
        student4.setId(1L);
        student4.setLastName("LastName");
        student4.setModifiedBy("Anonymous");
        student4.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student4.setPassword("SuperSecuredPassword");
        student4.setRole(Roles.ADMIN);
        student4.setStudentCourses(new HashSet<>());

        Submission.SubmissionId submissionId2 = new Submission.SubmissionId();
        submissionId2.setLessonId(1L);
        submissionId2.setStudentId(1L);

        Submission submission2 = new Submission();
        submission2.setCreatedBy("Anonymous");
        submission2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission2.setGrade(10.0d);
        submission2.setLesson(lesson3);
        submission2.setModifiedBy("Anonymous");
        submission2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission2.setStudent(student4);
        submission2.setSubmissionId(submissionId2);
        when(submissionMapper.fromRequestDto(Mockito.<SubmissionRequestDTO>any())).thenReturn(submission2);
        SubmissionResponseDTO submissionResponseDTO = new SubmissionResponseDTO();
        when(submissionMapper.toResponseDto(Mockito.<Submission>any())).thenReturn(submissionResponseDTO);
        assertSame(submissionResponseDTO, submissionServiceImpl.save(new SubmissionRequestDTO()));
        verify(submissionRepository).save(Mockito.<Submission>any());
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(lesson2).getCourse();
        verify(lesson2).setCreatedBy(Mockito.<String>any());
        verify(lesson2).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(lesson2).setModifiedBy(Mockito.<String>any());
        verify(lesson2).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(lesson2).setCourse(Mockito.<Course>any());
        verify(lesson2).setDescription(Mockito.<String>any());
        verify(lesson2).setId(Mockito.<Long>any());
        verify(lesson2).setSubmissions(Mockito.<Set<Submission>>any());
        verify(lesson2).setTitle(Mockito.<String>any());
        verify(studentRepository).findById(Mockito.<Long>any());
        verify(submissionMapper).fromRequestDto(Mockito.<SubmissionRequestDTO>any());
        verify(submissionMapper).toResponseDto(Mockito.<Submission>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#save(SubmissionRequestDTO)}
     */
    @Test
    void testSave4() {
        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");

        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());

        Submission.SubmissionId submissionId = new Submission.SubmissionId();
        submissionId.setLessonId(1L);
        submissionId.setStudentId(1L);

        Submission submission = new Submission();
        submission.setCreatedBy("Anonymous");
        submission.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission.setGrade(10.0d);
        submission.setLesson(lesson);
        submission.setModifiedBy("Anonymous");
        submission.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission.setStudent(student);
        submission.setSubmissionId(submissionId);
        when(submissionRepository.save(Mockito.<Submission>any())).thenReturn(submission);

        Course course2 = new Course();
        course2.setAvailable(true);
        course2.setCourseFeedbacks(new HashSet<>());
        course2.setCreatedBy("Anonymous");
        course2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course2.setDescription("The characteristics of someone or something");
        course2.setId(1L);
        course2.setInstructors(new HashSet<>());
        course2.setLessons(new HashSet<>());
        course2.setModifiedBy("Anonymous");
        course2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course2.setStudents(new HashSet<>());
        course2.setTitle("Dr");

        Student student2 = new Student();
        student2.setCreatedBy("Anonymous");
        student2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student2.setEmail("boom.boom@courseapi.org");
        student2.setFirstName("FirstName");
        student2.setId(1L);
        student2.setLastName("LastName");
        student2.setModifiedBy("Anonymous");
        student2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student2.setPassword("SuperSecuredPassword");
        student2.setRole(Roles.ADMIN);
        student2.setStudentCourses(new HashSet<>());

        HashSet<Student> students = new HashSet<>();
        students.add(student2);

        Course course3 = new Course();
        course3.setAvailable(true);
        course3.setCourseFeedbacks(new HashSet<>());
        course3.setCreatedBy("Anonymous");
        course3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course3.setDescription("The characteristics of someone or something");
        course3.setId(1L);
        course3.setInstructors(new HashSet<>());
        course3.setLessons(new HashSet<>());
        course3.setModifiedBy("Anonymous");
        course3.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course3.setStudents(students);
        course3.setTitle("Dr");
        Lesson lesson2 = mock(Lesson.class);
        when(lesson2.getCourse()).thenReturn(course3);
        doNothing().when(lesson2).setCreatedBy(Mockito.<String>any());
        doNothing().when(lesson2).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(lesson2).setModifiedBy(Mockito.<String>any());
        doNothing().when(lesson2).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(lesson2).setCourse(Mockito.<Course>any());
        doNothing().when(lesson2).setDescription(Mockito.<String>any());
        doNothing().when(lesson2).setId(Mockito.<Long>any());
        doNothing().when(lesson2).setSubmissions(Mockito.<Set<Submission>>any());
        doNothing().when(lesson2).setTitle(Mockito.<String>any());
        lesson2.setCourse(course2);
        lesson2.setCreatedBy("Anonymous");
        lesson2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setDescription("The characteristics of someone or something");
        lesson2.setId(1L);
        lesson2.setModifiedBy("Anonymous");
        lesson2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setSubmissions(new HashSet<>());
        lesson2.setTitle("Dr");
        Optional<Lesson> ofResult = Optional.of(lesson2);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Student student3 = new Student();
        student3.setCreatedBy("Anonymous");
        student3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student3.setEmail("boom.boom@courseapi.org");
        student3.setFirstName("FirstName");
        student3.setId(1L);
        student3.setLastName("LastName");
        student3.setModifiedBy("Anonymous");
        student3.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student3.setPassword("SuperSecuredPassword");
        student3.setRole(Roles.ADMIN);
        student3.setStudentCourses(new HashSet<>());
        Optional<Student> ofResult2 = Optional.of(student3);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(submissionMapper.fromRequestDto(Mockito.<SubmissionRequestDTO>any()))
                .thenThrow(new SystemException(ErrorCode.OK));
        when(submissionMapper.toResponseDto(Mockito.<Submission>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> submissionServiceImpl.save(new SubmissionRequestDTO()));
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(lesson2).getCourse();
        verify(lesson2).setCreatedBy(Mockito.<String>any());
        verify(lesson2).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(lesson2).setModifiedBy(Mockito.<String>any());
        verify(lesson2).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(lesson2).setCourse(Mockito.<Course>any());
        verify(lesson2).setDescription(Mockito.<String>any());
        verify(lesson2).setId(Mockito.<Long>any());
        verify(lesson2).setSubmissions(Mockito.<Set<Submission>>any());
        verify(lesson2).setTitle(Mockito.<String>any());
        verify(studentRepository).findById(Mockito.<Long>any());
        verify(submissionMapper).fromRequestDto(Mockito.<SubmissionRequestDTO>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findAll()}
     */
    @Test
    void testFindAll() {
        when(submissionRepository.findAll()).thenReturn(new ArrayList<>());
        ArrayList<SubmissionResponseDTO> submissionResponseDTOList = new ArrayList<>();
        when(submissionMapper.toResponseDto(Mockito.<List<Submission>>any())).thenReturn(submissionResponseDTOList);
        List<SubmissionResponseDTO> actualFindAllResult = submissionServiceImpl.findAll();
        assertSame(submissionResponseDTOList, actualFindAllResult);
        assertTrue(actualFindAllResult.isEmpty());
        verify(submissionRepository).findAll();
        verify(submissionMapper).toResponseDto(Mockito.<List<Submission>>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findAll()}
     */
    @Test
    void testFindAll2() {
        when(submissionRepository.findAll()).thenReturn(new ArrayList<>());
        when(submissionMapper.toResponseDto(Mockito.<List<Submission>>any()))
                .thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> submissionServiceImpl.findAll());
        verify(submissionRepository).findAll();
        verify(submissionMapper).toResponseDto(Mockito.<List<Submission>>any());
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
     * Method under test: {@link SubmissionServiceImpl#delete(Long, Long)}
     */
    @Test
    void testDelete2() {
        doThrow(new SystemException(ErrorCode.OK)).when(submissionRepository)
                .deleteBySubmissionId_LessonIdAndSubmissionId_StudentId(Mockito.<Long>any(), Mockito.<Long>any());
        assertThrows(SystemException.class, () -> submissionServiceImpl.delete(1L, 1L));
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

    /**
     * Method under test: {@link SubmissionServiceImpl#findAllByStudentIdAndCourseId(Long, Long)}
     */
    @Test
    void testFindAllByStudentIdAndCourseId2() {
        when(submissionRepository.findAllByStudentIdAndLessonCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> submissionServiceImpl.findAllByStudentIdAndCourseId(1L, 1L));
        verify(submissionRepository).findAllByStudentIdAndLessonCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#saveGrade(Long, Long, Double)}
     */
    @Test
    void testSaveGrade() {
        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");
        Optional<Lesson> ofResult = Optional.of(lesson);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());
        Optional<Student> ofResult2 = Optional.of(student);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(SystemException.class, () -> submissionServiceImpl.saveGrade(1L, 1L, 10.0d));
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(studentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#saveGrade(Long, Long, Double)}
     */
    @Test
    void testSaveGrade2() {
        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");
        Optional<Lesson> ofResult = Optional.of(lesson);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(studentRepository.findById(Mockito.<Long>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> submissionServiceImpl.saveGrade(1L, 1L, 10.0d));
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(studentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#saveGrade(Long, Long, Double)}
     */
    @Test
    void testSaveGrade3() {
        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");

        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());

        Submission.SubmissionId submissionId = new Submission.SubmissionId();
        submissionId.setLessonId(1L);
        submissionId.setStudentId(1L);

        Submission submission = new Submission();
        submission.setCreatedBy("Anonymous");
        submission.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission.setGrade(10.0d);
        submission.setLesson(lesson);
        submission.setModifiedBy("Anonymous");
        submission.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission.setStudent(student);
        submission.setSubmissionId(submissionId);
        when(submissionRepository.save(Mockito.<Submission>any())).thenReturn(submission);

        Course course2 = new Course();
        course2.setAvailable(true);
        course2.setCourseFeedbacks(new HashSet<>());
        course2.setCreatedBy("Anonymous");
        course2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course2.setDescription("The characteristics of someone or something");
        course2.setId(1L);
        course2.setInstructors(new HashSet<>());
        course2.setLessons(new HashSet<>());
        course2.setModifiedBy("Anonymous");
        course2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course2.setStudents(new HashSet<>());
        course2.setTitle("Dr");

        Student student2 = new Student();
        student2.setCreatedBy("Anonymous");
        student2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student2.setEmail("boom.boom@courseapi.org");
        student2.setFirstName("FirstName");
        student2.setId(1L);
        student2.setLastName("LastName");
        student2.setModifiedBy("Anonymous");
        student2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student2.setPassword("SuperSecuredPassword");
        student2.setRole(Roles.ADMIN);
        student2.setStudentCourses(new HashSet<>());

        HashSet<Student> students = new HashSet<>();
        students.add(student2);

        Course course3 = new Course();
        course3.setAvailable(true);
        course3.setCourseFeedbacks(new HashSet<>());
        course3.setCreatedBy("Anonymous");
        course3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course3.setDescription("The characteristics of someone or something");
        course3.setId(1L);
        course3.setInstructors(new HashSet<>());
        course3.setLessons(new HashSet<>());
        course3.setModifiedBy("Anonymous");
        course3.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course3.setStudents(students);
        course3.setTitle("Dr");
        Lesson lesson2 = mock(Lesson.class);
        when(lesson2.getCourse()).thenReturn(course3);
        doNothing().when(lesson2).setCreatedBy(Mockito.<String>any());
        doNothing().when(lesson2).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(lesson2).setModifiedBy(Mockito.<String>any());
        doNothing().when(lesson2).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(lesson2).setCourse(Mockito.<Course>any());
        doNothing().when(lesson2).setDescription(Mockito.<String>any());
        doNothing().when(lesson2).setId(Mockito.<Long>any());
        doNothing().when(lesson2).setSubmissions(Mockito.<Set<Submission>>any());
        doNothing().when(lesson2).setTitle(Mockito.<String>any());
        lesson2.setCourse(course2);
        lesson2.setCreatedBy("Anonymous");
        lesson2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setDescription("The characteristics of someone or something");
        lesson2.setId(1L);
        lesson2.setModifiedBy("Anonymous");
        lesson2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setSubmissions(new HashSet<>());
        lesson2.setTitle("Dr");
        Optional<Lesson> ofResult = Optional.of(lesson2);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Student student3 = new Student();
        student3.setCreatedBy("Anonymous");
        student3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student3.setEmail("boom.boom@courseapi.org");
        student3.setFirstName("FirstName");
        student3.setId(1L);
        student3.setLastName("LastName");
        student3.setModifiedBy("Anonymous");
        student3.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student3.setPassword("SuperSecuredPassword");
        student3.setRole(Roles.ADMIN);
        student3.setStudentCourses(new HashSet<>());
        Optional<Student> ofResult2 = Optional.of(student3);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        Course course4 = new Course();
        course4.setAvailable(true);
        course4.setCourseFeedbacks(new HashSet<>());
        course4.setCreatedBy("Anonymous");
        course4.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course4.setDescription("The characteristics of someone or something");
        course4.setId(1L);
        course4.setInstructors(new HashSet<>());
        course4.setLessons(new HashSet<>());
        course4.setModifiedBy("Anonymous");
        course4.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course4.setStudents(new HashSet<>());
        course4.setTitle("Dr");

        Lesson lesson3 = new Lesson();
        lesson3.setCourse(course4);
        lesson3.setCreatedBy("Anonymous");
        lesson3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson3.setDescription("The characteristics of someone or something");
        lesson3.setId(1L);
        lesson3.setModifiedBy("Anonymous");
        lesson3.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson3.setSubmissions(new HashSet<>());
        lesson3.setTitle("Dr");

        Student student4 = new Student();
        student4.setCreatedBy("Anonymous");
        student4.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student4.setEmail("boom.boom@courseapi.org");
        student4.setFirstName("FirstName");
        student4.setId(1L);
        student4.setLastName("LastName");
        student4.setModifiedBy("Anonymous");
        student4.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student4.setPassword("SuperSecuredPassword");
        student4.setRole(Roles.ADMIN);
        student4.setStudentCourses(new HashSet<>());

        Submission.SubmissionId submissionId2 = new Submission.SubmissionId();
        submissionId2.setLessonId(1L);
        submissionId2.setStudentId(1L);

        Submission submission2 = new Submission();
        submission2.setCreatedBy("Anonymous");
        submission2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission2.setGrade(10.0d);
        submission2.setLesson(lesson3);
        submission2.setModifiedBy("Anonymous");
        submission2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission2.setStudent(student4);
        submission2.setSubmissionId(submissionId2);
        when(submissionMapper.fromRequestDto(Mockito.<SubmissionRequestDTO>any())).thenReturn(submission2);
        SubmissionResponseDTO submissionResponseDTO = new SubmissionResponseDTO();
        when(submissionMapper.toResponseDto(Mockito.<Submission>any())).thenReturn(submissionResponseDTO);
        assertSame(submissionResponseDTO, submissionServiceImpl.saveGrade(1L, 1L, 10.0d));
        verify(submissionRepository).save(Mockito.<Submission>any());
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(lesson2).getCourse();
        verify(lesson2).setCreatedBy(Mockito.<String>any());
        verify(lesson2).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(lesson2).setModifiedBy(Mockito.<String>any());
        verify(lesson2).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(lesson2).setCourse(Mockito.<Course>any());
        verify(lesson2).setDescription(Mockito.<String>any());
        verify(lesson2).setId(Mockito.<Long>any());
        verify(lesson2).setSubmissions(Mockito.<Set<Submission>>any());
        verify(lesson2).setTitle(Mockito.<String>any());
        verify(studentRepository).findById(Mockito.<Long>any());
        verify(submissionMapper).fromRequestDto(Mockito.<SubmissionRequestDTO>any());
        verify(submissionMapper).toResponseDto(Mockito.<Submission>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findAllByLesson(Long, Long)}
     */
    @Test
    void testFindAllByLesson() {
        when(submissionRepository.findAllByLessonId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<SubmissionResponseDTO> submissionResponseDTOList = new ArrayList<>();
        when(submissionMapper.toResponseDto(Mockito.<List<Submission>>any())).thenReturn(submissionResponseDTOList);
        List<SubmissionResponseDTO> actualFindAllByLessonResult = submissionServiceImpl.findAllByLesson(1L, 1L);
        assertSame(submissionResponseDTOList, actualFindAllByLessonResult);
        assertTrue(actualFindAllByLessonResult.isEmpty());
        verify(submissionRepository).findAllByLessonId(Mockito.<Long>any());
        verify(submissionMapper).toResponseDto(Mockito.<List<Submission>>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findAllByLesson(Long, Long)}
     */
    @Test
    void testFindAllByLesson2() {
        when(submissionRepository.findAllByLessonId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(submissionMapper.toResponseDto(Mockito.<List<Submission>>any()))
                .thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> submissionServiceImpl.findAllByLesson(1L, 1L));
        verify(submissionRepository).findAllByLessonId(Mockito.<Long>any());
        verify(submissionMapper).toResponseDto(Mockito.<List<Submission>>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findAllByStudent(Long, Long)}
     */
    @Test
    void testFindAllByStudent() {
        User user = new User();
        user.setCreatedBy("Anonymous");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("boom.boom@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Anonymous");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("SuperSecuredPassword");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(SystemException.class, () -> submissionServiceImpl.findAllByStudent(1L, 1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findAllByStudent(Long, Long)}
     */
    @Test
    void testFindAllByStudent2() {
        when(submissionRepository.findAllByStudentId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<SubmissionResponseDTO> submissionResponseDTOList = new ArrayList<>();
        when(submissionMapper.toResponseDto(Mockito.<List<Submission>>any())).thenReturn(submissionResponseDTOList);

        Admin admin = new Admin();
        admin.setCreatedBy("Anonymous");
        admin.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        admin.setEmail("boom.boom@courseapi.org");
        admin.setFirstName("FirstName");
        admin.setId(1L);
        admin.setLastName("LastName");
        admin.setModifiedBy("Anonymous");
        admin.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        admin.setPassword("SuperSecuredPassword");
        admin.setRole(Roles.ADMIN);
        admin.setCreatedBy("Anonymous");
        admin.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        admin.setEmail("boom.boom@courseapi.org");
        admin.setFirstName("FirstName");
        admin.setId(1L);
        admin.setLastName("LastName");
        admin.setModifiedBy("Anonymous");
        admin.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        admin.setPassword("SuperSecuredPassword");
        admin.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(admin);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        List<SubmissionResponseDTO> actualFindAllByStudentResult = submissionServiceImpl.findAllByStudent(1L, 1L);
        assertSame(submissionResponseDTOList, actualFindAllByStudentResult);
        assertTrue(actualFindAllByStudentResult.isEmpty());
        verify(submissionRepository).findAllByStudentId(Mockito.<Long>any());
        verify(submissionMapper).toResponseDto(Mockito.<List<Submission>>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findAllByStudent(Long, Long)}
     */
    @Test
    void testFindAllByStudent3() {
        when(submissionRepository.findAllByStudentId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(submissionMapper.toResponseDto(Mockito.<List<Submission>>any()))
                .thenThrow(new SystemException(ErrorCode.OK));

        Admin admin = new Admin();
        admin.setCreatedBy("Anonymous");
        admin.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        admin.setEmail("boom.boom@courseapi.org");
        admin.setFirstName("FirstName");
        admin.setId(1L);
        admin.setLastName("LastName");
        admin.setModifiedBy("Anonymous");
        admin.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        admin.setPassword("SuperSecuredPassword");
        admin.setRole(Roles.ADMIN);
        admin.setCreatedBy("Anonymous");
        admin.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        admin.setEmail("boom.boom@courseapi.org");
        admin.setFirstName("FirstName");
        admin.setId(1L);
        admin.setLastName("LastName");
        admin.setModifiedBy("Anonymous");
        admin.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        admin.setPassword("SuperSecuredPassword");
        admin.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(admin);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(SystemException.class, () -> submissionServiceImpl.findAllByStudent(1L, 1L));
        verify(submissionRepository).findAllByStudentId(Mockito.<Long>any());
        verify(submissionMapper).toResponseDto(Mockito.<List<Submission>>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findAllByStudent(Long, Long)}
     */
    @Test
    void testFindAllByStudent4() {
        when(submissionRepository.findAllByStudentId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(submissionMapper.toResponseDto(Mockito.<List<Submission>>any())).thenReturn(new ArrayList<>());

        Instructor instructor = new Instructor();
        instructor.setCreatedBy("Anonymous");
        instructor.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        instructor.setEmail("boom.boom@courseapi.org");
        instructor.setFirstName("FirstName");
        instructor.setId(1L);
        instructor.setInstructorCourses(new HashSet<>());
        instructor.setLastName("LastName");
        instructor.setModifiedBy("Anonymous");
        instructor.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        instructor.setPassword("SuperSecuredPassword");
        instructor.setRole(Roles.ADMIN);
        instructor.setCreatedBy("Anonymous");
        instructor.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        instructor.setEmail("boom.boom@courseapi.org");
        instructor.setFirstName("FirstName");
        instructor.setId(1L);
        instructor.setLastName("LastName");
        instructor.setModifiedBy("Anonymous");
        instructor.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        instructor.setPassword("SuperSecuredPassword");
        instructor.setRole(Roles.ADMIN);
        instructor.setCreatedBy("Anonymous");
        instructor.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        instructor.setEmail("boom.boom@courseapi.org");
        instructor.setFirstName("FirstName");
        instructor.setId(1L);
        instructor.setLastName("LastName");
        instructor.setModifiedBy("Anonymous");
        instructor.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        instructor.setPassword("SuperSecuredPassword");
        instructor.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(instructor);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertTrue(submissionServiceImpl.findAllByStudent(1L, 1L).isEmpty());
        verify(submissionRepository).findAllByStudentId(Mockito.<Long>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findAllByStudent(Long, Long)}
     */
    @Test
    void testFindAllByStudent5() {
        when(submissionRepository.findAllByStudentId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<SubmissionResponseDTO> submissionResponseDTOList = new ArrayList<>();
        when(submissionMapper.toResponseDto(Mockito.<List<Submission>>any())).thenReturn(submissionResponseDTOList);

        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(student);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        List<SubmissionResponseDTO> actualFindAllByStudentResult = submissionServiceImpl.findAllByStudent(1L, 1L);
        assertSame(submissionResponseDTOList, actualFindAllByStudentResult);
        assertTrue(actualFindAllByStudentResult.isEmpty());
        verify(submissionRepository).findAllByStudentId(Mockito.<Long>any());
        verify(submissionMapper).toResponseDto(Mockito.<List<Submission>>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#findAllByStudent(Long, Long)}
     */
    @Test
    void testFindAllByStudent6() {
        when(submissionRepository.findAllByStudentId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(submissionMapper.toResponseDto(Mockito.<List<Submission>>any())).thenReturn(new ArrayList<>());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(SystemException.class, () -> submissionServiceImpl.findAllByStudent(1L, 1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#validateSubmission(Long, Long)}
     */
    @Test
    void testValidateSubmission() {
        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");
        Optional<Lesson> ofResult = Optional.of(lesson);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());
        Optional<Student> ofResult2 = Optional.of(student);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(SystemException.class, () -> submissionServiceImpl.validateSubmission(1L, 1L));
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(studentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#validateSubmission(Long, Long)}
     */
    @Test
    void testValidateSubmission2() {
        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");
        Optional<Lesson> ofResult = Optional.of(lesson);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(studentRepository.findById(Mockito.<Long>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> submissionServiceImpl.validateSubmission(1L, 1L));
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(studentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#validateSubmission(Long, Long)}
     */
    @Test
    void testValidateSubmission3() {
        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());

        HashSet<Student> students = new HashSet<>();
        students.add(student);

        Course course2 = new Course();
        course2.setAvailable(true);
        course2.setCourseFeedbacks(new HashSet<>());
        course2.setCreatedBy("Anonymous");
        course2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course2.setDescription("The characteristics of someone or something");
        course2.setId(1L);
        course2.setInstructors(new HashSet<>());
        course2.setLessons(new HashSet<>());
        course2.setModifiedBy("Anonymous");
        course2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course2.setStudents(students);
        course2.setTitle("Dr");
        Lesson lesson = mock(Lesson.class);
        when(lesson.getCourse()).thenReturn(course2);
        doNothing().when(lesson).setCreatedBy(Mockito.<String>any());
        doNothing().when(lesson).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(lesson).setModifiedBy(Mockito.<String>any());
        doNothing().when(lesson).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(lesson).setCourse(Mockito.<Course>any());
        doNothing().when(lesson).setDescription(Mockito.<String>any());
        doNothing().when(lesson).setId(Mockito.<Long>any());
        doNothing().when(lesson).setSubmissions(Mockito.<Set<Submission>>any());
        doNothing().when(lesson).setTitle(Mockito.<String>any());
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");
        Optional<Lesson> ofResult = Optional.of(lesson);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Student student2 = new Student();
        student2.setCreatedBy("Anonymous");
        student2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student2.setEmail("boom.boom@courseapi.org");
        student2.setFirstName("FirstName");
        student2.setId(1L);
        student2.setLastName("LastName");
        student2.setModifiedBy("Anonymous");
        student2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student2.setPassword("SuperSecuredPassword");
        student2.setRole(Roles.ADMIN);
        student2.setStudentCourses(new HashSet<>());
        Optional<Student> ofResult2 = Optional.of(student2);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        submissionServiceImpl.validateSubmission(1L, 1L);
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(lesson).getCourse();
        verify(lesson).setCreatedBy(Mockito.<String>any());
        verify(lesson).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(lesson).setModifiedBy(Mockito.<String>any());
        verify(lesson).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(lesson).setCourse(Mockito.<Course>any());
        verify(lesson).setDescription(Mockito.<String>any());
        verify(lesson).setId(Mockito.<Long>any());
        verify(lesson).setSubmissions(Mockito.<Set<Submission>>any());
        verify(lesson).setTitle(Mockito.<String>any());
        verify(studentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SubmissionServiceImpl#validateSubmission(Long, Long)}
     */
    @Test
    void testValidateSubmission4() {
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());

        Course course = new Course();
        course.setAvailable(true);
        course.setCourseFeedbacks(new HashSet<>());
        course.setCreatedBy("Anonymous");
        course.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructors(new HashSet<>());
        course.setLessons(new HashSet<>());
        course.setModifiedBy("Anonymous");
        course.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course.setStudents(new HashSet<>());
        course.setTitle("Dr");

        Course course2 = new Course();
        course2.setAvailable(true);
        course2.setCourseFeedbacks(new HashSet<>());
        course2.setCreatedBy("Anonymous");
        course2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course2.setDescription("The characteristics of someone or something");
        course2.setId(1L);
        course2.setInstructors(new HashSet<>());
        course2.setLessons(new HashSet<>());
        course2.setModifiedBy("Anonymous");
        course2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course2.setStudents(new HashSet<>());
        course2.setTitle("Dr");
        Lesson lesson = mock(Lesson.class);
        when(lesson.getCourse()).thenReturn(course2);
        doNothing().when(lesson).setCreatedBy(Mockito.<String>any());
        doNothing().when(lesson).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(lesson).setModifiedBy(Mockito.<String>any());
        doNothing().when(lesson).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(lesson).setCourse(Mockito.<Course>any());
        doNothing().when(lesson).setDescription(Mockito.<String>any());
        doNothing().when(lesson).setId(Mockito.<Long>any());
        doNothing().when(lesson).setSubmissions(Mockito.<Set<Submission>>any());
        doNothing().when(lesson).setTitle(Mockito.<String>any());
        lesson.setCourse(course);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");

        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());
        Optional<Student> ofResult = Optional.of(student);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(SystemException.class, () -> submissionServiceImpl.validateSubmission(1L, 1L));
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(lesson).setCreatedBy(Mockito.<String>any());
        verify(lesson).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(lesson).setModifiedBy(Mockito.<String>any());
        verify(lesson).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(lesson).setCourse(Mockito.<Course>any());
        verify(lesson).setDescription(Mockito.<String>any());
        verify(lesson).setId(Mockito.<Long>any());
        verify(lesson).setSubmissions(Mockito.<Set<Submission>>any());
        verify(lesson).setTitle(Mockito.<String>any());
    }
}

