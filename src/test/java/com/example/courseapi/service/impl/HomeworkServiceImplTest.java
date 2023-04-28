package com.example.courseapi.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.generic.FiltersImpl;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Homework;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.HomeworkDTO;
import com.example.courseapi.exception.LessonNotFoundException;
import com.example.courseapi.exception.StudentNotSubscribedToCourse;
import com.example.courseapi.repository.HomeworkRepository;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.service.mapper.HomeworkMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {HomeworkServiceImpl.class})
@ExtendWith(SpringExtension.class)
class HomeworkServiceImplTest {
    @MockBean
    private HomeworkMapper homeworkMapper;

    @MockBean
    private HomeworkRepository homeworkRepository;

    @Autowired
    private HomeworkServiceImpl homeworkServiceImpl;

    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private StudentRepository studentRepository;

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
     * Method under test: {@link HomeworkServiceImpl#findById(Long)}
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

        Homework homework = new Homework();
        homework.setCreatedBy("username");
        homework.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homework.setFilePath("/directory/foo.txt");
        homework.setId(1L);
        homework.setLesson(lesson);
        homework.setModifiedBy("username");
        homework.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homework.setStudent(student);
        homework.setTitle("Dr");
        Optional<Homework> ofResult = Optional.of(homework);
        when(homeworkRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        HomeworkDTO homeworkDTO = new HomeworkDTO();
        homeworkDTO.setCreatedBy("username");
        homeworkDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homeworkDTO.setFilePath("/directory/foo.txt");
        homeworkDTO.setId(1L);
        homeworkDTO.setLessonId(1L);
        homeworkDTO.setModifiedBy("username");
        homeworkDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homeworkDTO.setStudentId(1L);
        homeworkDTO.setTitle("Dr");
        when(homeworkMapper.toDto(Mockito.<Homework>any())).thenReturn(homeworkDTO);
        assertTrue(homeworkServiceImpl.findById(1L).isPresent());
        verify(homeworkRepository).findById(Mockito.<Long>any());
        verify(homeworkMapper).toDto(Mockito.<Homework>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#save(HomeworkDTO)}
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

        Homework homework = new Homework();
        homework.setCreatedBy("username");
        homework.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homework.setFilePath("/directory/foo.txt");
        homework.setId(1L);
        homework.setLesson(lesson);
        homework.setModifiedBy("username");
        homework.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homework.setStudent(student);
        homework.setTitle("Dr");
        when(homeworkRepository.save(Mockito.<Homework>any())).thenReturn(homework);

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

        Homework homework2 = new Homework();
        homework2.setCreatedBy("username");
        homework2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homework2.setFilePath("/directory/foo.txt");
        homework2.setId(1L);
        homework2.setLesson(lesson2);
        homework2.setModifiedBy("username");
        homework2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homework2.setStudent(student2);
        homework2.setTitle("Dr");

        HomeworkDTO homeworkDTO = new HomeworkDTO();
        homeworkDTO.setCreatedBy("username");
        homeworkDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homeworkDTO.setFilePath("/directory/foo.txt");
        homeworkDTO.setId(1L);
        homeworkDTO.setLessonId(1L);
        homeworkDTO.setModifiedBy("username");
        homeworkDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homeworkDTO.setStudentId(1L);
        homeworkDTO.setTitle("Dr");
        when(homeworkMapper.toEntity(Mockito.<HomeworkDTO>any())).thenReturn(homework2);
        when(homeworkMapper.toDto(Mockito.<Homework>any())).thenReturn(homeworkDTO);

        HomeworkDTO homeworkDTO2 = new HomeworkDTO();
        homeworkDTO2.setCreatedBy("username");
        homeworkDTO2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homeworkDTO2.setFilePath("/directory/foo.txt");
        homeworkDTO2.setId(1L);
        homeworkDTO2.setLessonId(1L);
        homeworkDTO2.setModifiedBy("username");
        homeworkDTO2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homeworkDTO2.setStudentId(1L);
        homeworkDTO2.setTitle("Dr");
        assertSame(homeworkDTO, homeworkServiceImpl.save(homeworkDTO2));
        verify(homeworkRepository).save(Mockito.<Homework>any());
        verify(homeworkMapper).toEntity(Mockito.<HomeworkDTO>any());
        verify(homeworkMapper).toDto(Mockito.<Homework>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#save(HomeworkDTO)}
     */
    @Test
    void testSave2() {
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

        Homework homework = new Homework();
        homework.setCreatedBy("username");
        homework.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homework.setFilePath("/directory/foo.txt");
        homework.setId(1L);
        homework.setLesson(lesson);
        homework.setModifiedBy("username");
        homework.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homework.setStudent(student);
        homework.setTitle("Dr");
        when(homeworkRepository.save(Mockito.<Homework>any())).thenReturn(homework);
        when(homeworkMapper.toEntity(Mockito.<HomeworkDTO>any())).thenThrow(new LessonNotFoundException());
        when(homeworkMapper.toDto(Mockito.<Homework>any())).thenThrow(new LessonNotFoundException());

        HomeworkDTO homeworkDTO = new HomeworkDTO();
        homeworkDTO.setCreatedBy("username");
        homeworkDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homeworkDTO.setFilePath("/directory/foo.txt");
        homeworkDTO.setId(1L);
        homeworkDTO.setLessonId(1L);
        homeworkDTO.setModifiedBy("username");
        homeworkDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homeworkDTO.setStudentId(1L);
        homeworkDTO.setTitle("Dr");
        assertThrows(LessonNotFoundException.class, () -> homeworkServiceImpl.save(homeworkDTO));
        verify(homeworkMapper).toEntity(Mockito.<HomeworkDTO>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findAll(Filters, Pageable)}
     */
    @Test
    void testFindAll() {
        when(homeworkRepository.findAll(Mockito.<Specification<Homework>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(homeworkServiceImpl.findAll(new FiltersImpl(), null).toList().isEmpty());
        verify(homeworkRepository).findAll(Mockito.<Specification<Homework>>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#delete(Long)}
     */
    @Test
    void testDelete() {
        doNothing().when(homeworkRepository).deleteById(Mockito.<Long>any());
        homeworkServiceImpl.delete(1L);
        verify(homeworkRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#delete(Long)}
     */
    @Test
    void testDelete2() {
        doThrow(new StudentNotSubscribedToCourse()).when(homeworkRepository).deleteById(Mockito.<Long>any());
        assertThrows(StudentNotSubscribedToCourse.class, () -> homeworkServiceImpl.delete(1L));
        verify(homeworkRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findByStudent(Long)}
     */
    @Test
    void testFindByStudent() {
        when(homeworkRepository.findByStudentId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<HomeworkDTO> homeworkDTOList = new ArrayList<>();
        when(homeworkMapper.toDto(Mockito.<List<Homework>>any())).thenReturn(homeworkDTOList);
        List<HomeworkDTO> actualFindByStudentResult = homeworkServiceImpl.findByStudent(1L);
        assertSame(homeworkDTOList, actualFindByStudentResult);
        assertTrue(actualFindByStudentResult.isEmpty());
        verify(homeworkRepository).findByStudentId(Mockito.<Long>any());
        verify(homeworkMapper).toDto(Mockito.<List<Homework>>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findByStudent(Long)}
     */
    @Test
    void testFindByStudent2() {
        when(homeworkRepository.findByStudentId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(homeworkMapper.toDto(Mockito.<List<Homework>>any())).thenThrow(new StudentNotSubscribedToCourse());
        assertThrows(StudentNotSubscribedToCourse.class, () -> homeworkServiceImpl.findByStudent(1L));
        verify(homeworkRepository).findByStudentId(Mockito.<Long>any());
        verify(homeworkMapper).toDto(Mockito.<List<Homework>>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findByLessonId(Long)}
     */
    @Test
    void testFindByLessonId() {
        when(homeworkRepository.findByLessonId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<HomeworkDTO> homeworkDTOList = new ArrayList<>();
        when(homeworkMapper.toDto(Mockito.<List<Homework>>any())).thenReturn(homeworkDTOList);
        List<HomeworkDTO> actualFindByLessonIdResult = homeworkServiceImpl.findByLessonId(1L);
        assertSame(homeworkDTOList, actualFindByLessonIdResult);
        assertTrue(actualFindByLessonIdResult.isEmpty());
        verify(homeworkRepository).findByLessonId(Mockito.<Long>any());
        verify(homeworkMapper).toDto(Mockito.<List<Homework>>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findByLessonId(Long)}
     */
    @Test
    void testFindByLessonId2() {
        when(homeworkRepository.findByLessonId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(homeworkMapper.toDto(Mockito.<List<Homework>>any())).thenThrow(new StudentNotSubscribedToCourse());
        assertThrows(StudentNotSubscribedToCourse.class, () -> homeworkServiceImpl.findByLessonId(1L));
        verify(homeworkRepository).findByLessonId(Mockito.<Long>any());
        verify(homeworkMapper).toDto(Mockito.<List<Homework>>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#uploadHomeworkForLesson(Long, MultipartFile, Long)}
     */
    @Test
    void testUploadHomeworkForLesson() throws IOException {
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
        Optional<Student> ofResult = Optional.of(student);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        Optional<Lesson> ofResult2 = Optional.of(lesson);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(StudentNotSubscribedToCourse.class, () -> homeworkServiceImpl.uploadHomeworkForLesson(1L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))), 1L));
        verify(studentRepository).findById(Mockito.<Long>any());
        verify(lessonRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#uploadHomeworkForLesson(Long, MultipartFile, Long)}
     */
    @Test
    void testUploadHomeworkForLesson2() throws IOException {
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
        Optional<Student> ofResult = Optional.of(student);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(lessonRepository.findById(Mockito.<Long>any())).thenThrow(new StudentNotSubscribedToCourse());
        assertThrows(StudentNotSubscribedToCourse.class, () -> homeworkServiceImpl.uploadHomeworkForLesson(1L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))), 1L));
        verify(studentRepository).findById(Mockito.<Long>any());
        verify(lessonRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#uploadHomeworkForLesson(Long, MultipartFile, Long)}
     */
    @Test
    void testUploadHomeworkForLesson3() throws IOException {
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

        Homework homework = new Homework();
        homework.setCreatedBy("username");
        homework.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homework.setFilePath("/directory/foo.txt");
        homework.setId(1L);
        homework.setLesson(lesson);
        homework.setModifiedBy("username");
        homework.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homework.setStudent(student);
        homework.setTitle("Dr");
        when(homeworkRepository.save(Mockito.<Homework>any())).thenReturn(homework);

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

        HashSet<Course> studentCourses = new HashSet<>();
        studentCourses.add(course2);

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
        student2.setStudentCourses(studentCourses);
        Optional<Student> ofResult = Optional.of(student2);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Course course3 = new Course();
        course3.setCourseFeedbacks(new HashSet<>());
        course3.setCreatedBy("username");
        course3.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course3.setDescription("The characteristics of someone or something");
        course3.setId(1L);
        course3.setInstructors(new HashSet<>());
        course3.setLessons(new HashSet<>());
        course3.setModifiedBy("username");
        course3.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course3.setStudents(new HashSet<>());
        course3.setTitle("Dr");

        Lesson lesson2 = new Lesson();
        lesson2.setCourse(course3);
        lesson2.setCreatedBy("username");
        lesson2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lesson2.setDescription("The characteristics of someone or something");
        lesson2.setId(1L);
        lesson2.setModifiedBy("username");
        lesson2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lesson2.setSubmissions(new HashSet<>());
        lesson2.setTitle("Dr");
        Optional<Lesson> ofResult2 = Optional.of(lesson2);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        HomeworkDTO homeworkDTO = new HomeworkDTO();
        homeworkDTO.setCreatedBy("username");
        homeworkDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homeworkDTO.setFilePath("/directory/foo.txt");
        homeworkDTO.setId(1L);
        homeworkDTO.setLessonId(1L);
        homeworkDTO.setModifiedBy("username");
        homeworkDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homeworkDTO.setStudentId(1L);
        homeworkDTO.setTitle("Dr");
        when(homeworkMapper.toDto(Mockito.<Homework>any())).thenReturn(homeworkDTO);
        assertSame(homeworkDTO, homeworkServiceImpl.uploadHomeworkForLesson(1L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))), 1L));
        verify(homeworkRepository).save(Mockito.<Homework>any());
        verify(studentRepository).findById(Mockito.<Long>any());
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(homeworkMapper).toDto(Mockito.<Homework>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#uploadHomeworkForLesson(Long, MultipartFile, Long)}
     */
    @Test
    void testUploadHomeworkForLesson4() throws IOException {
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

        Homework homework = new Homework();
        homework.setCreatedBy("username");
        homework.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homework.setFilePath("/directory/foo.txt");
        homework.setId(1L);
        homework.setLesson(lesson);
        homework.setModifiedBy("username");
        homework.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        homework.setStudent(student);
        homework.setTitle("Dr");
        when(homeworkRepository.save(Mockito.<Homework>any())).thenReturn(homework);

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

        HashSet<Course> studentCourses = new HashSet<>();
        studentCourses.add(course2);

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
        student2.setStudentCourses(studentCourses);
        Optional<Student> ofResult = Optional.of(student2);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Course course3 = new Course();
        course3.setCourseFeedbacks(new HashSet<>());
        course3.setCreatedBy("username");
        course3.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course3.setDescription("The characteristics of someone or something");
        course3.setId(1L);
        course3.setInstructors(new HashSet<>());
        course3.setLessons(new HashSet<>());
        course3.setModifiedBy("username");
        course3.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course3.setStudents(new HashSet<>());
        course3.setTitle("Dr");

        Lesson lesson2 = new Lesson();
        lesson2.setCourse(course3);
        lesson2.setCreatedBy("username");
        lesson2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lesson2.setDescription("The characteristics of someone or something");
        lesson2.setId(1L);
        lesson2.setModifiedBy("username");
        lesson2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lesson2.setSubmissions(new HashSet<>());
        lesson2.setTitle("Dr");
        Optional<Lesson> ofResult2 = Optional.of(lesson2);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(homeworkMapper.toDto(Mockito.<Homework>any())).thenThrow(new StudentNotSubscribedToCourse());
        assertThrows(StudentNotSubscribedToCourse.class, () -> homeworkServiceImpl.uploadHomeworkForLesson(1L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))), 1L));
        verify(homeworkRepository).save(Mockito.<Homework>any());
        verify(studentRepository).findById(Mockito.<Long>any());
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(homeworkMapper).toDto(Mockito.<Homework>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findAllByStudentIdAndCourseId(Long, Long)}
     */
    @Test
    void testFindAllByStudentIdAndCourseId() {
        ArrayList<Homework> homeworkList = new ArrayList<>();
        when(homeworkRepository.findByStudentIdAndLessonId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(homeworkList);
        List<Homework> actualFindAllByStudentIdAndCourseIdResult = homeworkServiceImpl.findAllByStudentIdAndCourseId(1L,
                1L);
        assertSame(homeworkList, actualFindAllByStudentIdAndCourseIdResult);
        assertTrue(actualFindAllByStudentIdAndCourseIdResult.isEmpty());
        verify(homeworkRepository).findByStudentIdAndLessonId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findAllByStudentIdAndCourseId(Long, Long)}
     */
    @Test
    void testFindAllByStudentIdAndCourseId2() {
        when(homeworkRepository.findByStudentIdAndLessonId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenThrow(new StudentNotSubscribedToCourse());
        assertThrows(StudentNotSubscribedToCourse.class,
                () -> homeworkServiceImpl.findAllByStudentIdAndCourseId(1L, 1L));
        verify(homeworkRepository).findByStudentIdAndLessonId(Mockito.<Long>any(), Mockito.<Long>any());
    }
}

