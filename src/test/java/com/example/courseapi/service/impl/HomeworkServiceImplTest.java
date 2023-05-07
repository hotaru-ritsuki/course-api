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
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.request.HomeworkRequestDTO;
import com.example.courseapi.dto.response.HomeworkResponseDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.HomeworkRepository;
import com.example.courseapi.repository.InstructorRepository;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.service.S3Service;
import com.example.courseapi.service.mapper.HomeworkMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
    private InstructorRepository instructorRepository;

    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private S3Service s3Service;

    /**
     * Method under test: {@link HomeworkServiceImpl#findById(Long)}
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

        Homework homework = new Homework();
        homework.setCreatedBy("Anonymous");
        homework.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setFilePath("/directory/foo.txt");
        homework.setId(1L);
        homework.setLesson(lesson);
        homework.setModifiedBy("Anonymous");
        homework.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setStudent(student);
        homework.setTitle("Dr");
        Optional<Homework> ofResult = Optional.of(homework);
        when(homeworkRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        HomeworkResponseDTO homeworkResponseDTO = new HomeworkResponseDTO();
        homeworkResponseDTO.setCreatedBy("Anonymous");
        homeworkResponseDTO.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homeworkResponseDTO.setFilePath("/directory/foo.txt");
        homeworkResponseDTO.setId(1L);
        homeworkResponseDTO.setLessonId(1L);
        homeworkResponseDTO.setModifiedBy("Anonymous");
        homeworkResponseDTO.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homeworkResponseDTO.setStudentId(1L);
        homeworkResponseDTO.setTitle("Dr");
        when(homeworkMapper.toResponseDto(Mockito.<Homework>any())).thenReturn(homeworkResponseDTO);
        assertTrue(homeworkServiceImpl.findById(1L).isPresent());
        verify(homeworkRepository).findById(Mockito.<Long>any());
        verify(homeworkMapper).toResponseDto(Mockito.<Homework>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findById(Long)}
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

        Homework homework = new Homework();
        homework.setCreatedBy("Anonymous");
        homework.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setFilePath("/directory/foo.txt");
        homework.setId(1L);
        homework.setLesson(lesson);
        homework.setModifiedBy("Anonymous");
        homework.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setStudent(student);
        homework.setTitle("Dr");
        Optional<Homework> ofResult = Optional.of(homework);
        when(homeworkRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        homeworkServiceImpl.findById(1L);
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#save(HomeworkRequestDTO)}
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

        Homework homework = new Homework();
        homework.setCreatedBy("Anonymous");
        homework.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setFilePath("/directory/foo.txt");
        homework.setId(1L);
        homework.setLesson(lesson);
        homework.setModifiedBy("Anonymous");
        homework.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setStudent(student);
        homework.setTitle("Dr");
        when(homeworkRepository.save(Mockito.<Homework>any())).thenReturn(homework);

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

        Lesson lesson2 = new Lesson();
        lesson2.setCourse(course2);
        lesson2.setCreatedBy("Anonymous");
        lesson2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setDescription("The characteristics of someone or something");
        lesson2.setId(1L);
        lesson2.setModifiedBy("Anonymous");
        lesson2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setSubmissions(new HashSet<>());
        lesson2.setTitle("Dr");

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

        Homework homework2 = new Homework();
        homework2.setCreatedBy("Anonymous");
        homework2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework2.setFilePath("/directory/foo.txt");
        homework2.setId(1L);
        homework2.setLesson(lesson2);
        homework2.setModifiedBy("Anonymous");
        homework2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework2.setStudent(student2);
        homework2.setTitle("Dr");

        HomeworkResponseDTO homeworkResponseDTO = new HomeworkResponseDTO();
        homeworkResponseDTO.setCreatedBy("Anonymous");
        homeworkResponseDTO.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homeworkResponseDTO.setFilePath("/directory/foo.txt");
        homeworkResponseDTO.setId(1L);
        homeworkResponseDTO.setLessonId(1L);
        homeworkResponseDTO.setModifiedBy("Anonymous");
        homeworkResponseDTO.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homeworkResponseDTO.setStudentId(1L);
        homeworkResponseDTO.setTitle("Dr");
        when(homeworkMapper.fromRequestDto(Mockito.<HomeworkRequestDTO>any())).thenReturn(homework2);
        when(homeworkMapper.toResponseDto(Mockito.<Homework>any())).thenReturn(homeworkResponseDTO);
        assertSame(homeworkResponseDTO, homeworkServiceImpl.save(new HomeworkRequestDTO()));
        verify(homeworkRepository).save(Mockito.<Homework>any());
        verify(homeworkMapper).fromRequestDto(Mockito.<HomeworkRequestDTO>any());
        verify(homeworkMapper).toResponseDto(Mockito.<Homework>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#save(HomeworkRequestDTO)}
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

        Homework homework = new Homework();
        homework.setCreatedBy("Anonymous");
        homework.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setFilePath("/directory/foo.txt");
        homework.setId(1L);
        homework.setLesson(lesson);
        homework.setModifiedBy("Anonymous");
        homework.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setStudent(student);
        homework.setTitle("Dr");
        when(homeworkRepository.save(Mockito.<Homework>any())).thenReturn(homework);
        when(homeworkMapper.fromRequestDto(Mockito.<HomeworkRequestDTO>any()))
                .thenThrow(new SystemException(ErrorCode.OK));
        when(homeworkMapper.toResponseDto(Mockito.<Homework>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> homeworkServiceImpl.save(new HomeworkRequestDTO()));
        verify(homeworkMapper).fromRequestDto(Mockito.<HomeworkRequestDTO>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findAll(Filters, Pageable, User)}
     */
    @Test
    void testFindAll() {
        when(homeworkRepository.findAll(Mockito.<Specification<Homework>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        FiltersImpl filters = new FiltersImpl();

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
        assertTrue(homeworkServiceImpl.findAll(filters, null, user).toList().isEmpty());
        verify(homeworkRepository).findAll(Mockito.<Specification<Homework>>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findAll(Filters, Pageable, User)}
     */
    @Test
    void testFindAll2() {

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

        Homework homework = new Homework();
        homework.setCreatedBy("Anonymous");
        homework.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setFilePath("/directory/foo.txt");
        homework.setId(1L);
        homework.setLesson(lesson);
        homework.setModifiedBy("Anonymous");
        homework.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setStudent(student);
        homework.setTitle("Dr");

        ArrayList<Homework> content = new ArrayList<>();
        content.add(homework);
        PageImpl<Homework> pageImpl = new PageImpl<>(content);
        when(homeworkRepository.findAll(Mockito.<Specification<Homework>>any(), Mockito.<Pageable>any()))
                .thenReturn(pageImpl);
        FiltersImpl filters = new FiltersImpl();

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
        homeworkServiceImpl.findAll(filters, null, user);
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
        doThrow(new SystemException(ErrorCode.OK)).when(homeworkRepository).deleteById(Mockito.<Long>any());
        assertThrows(SystemException.class, () -> homeworkServiceImpl.delete(1L));
        verify(homeworkRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findByStudent(Long)}
     */
    @Test
    void testFindByStudent() {
        when(homeworkRepository.findByStudentId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<HomeworkResponseDTO> homeworkResponseDTOList = new ArrayList<>();
        when(homeworkMapper.toResponseDto(Mockito.<List<Homework>>any())).thenReturn(homeworkResponseDTOList);
        List<HomeworkResponseDTO> actualFindByStudentResult = homeworkServiceImpl.findByStudent(1L);
        assertSame(homeworkResponseDTOList, actualFindByStudentResult);
        assertTrue(actualFindByStudentResult.isEmpty());
        verify(homeworkRepository).findByStudentId(Mockito.<Long>any());
        verify(homeworkMapper).toResponseDto(Mockito.<List<Homework>>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findByStudent(Long)}
     */
    @Test
    void testFindByStudent2() {
        when(homeworkRepository.findByStudentId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(homeworkMapper.toResponseDto(Mockito.<List<Homework>>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> homeworkServiceImpl.findByStudent(1L));
        verify(homeworkRepository).findByStudentId(Mockito.<Long>any());
        verify(homeworkMapper).toResponseDto(Mockito.<List<Homework>>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findByLessonId(Long)}
     */
    @Test
    void testFindByLessonId() {
        when(homeworkRepository.findByLessonId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<HomeworkResponseDTO> homeworkResponseDTOList = new ArrayList<>();
        when(homeworkMapper.toResponseDto(Mockito.<List<Homework>>any())).thenReturn(homeworkResponseDTOList);
        List<HomeworkResponseDTO> actualFindByLessonIdResult = homeworkServiceImpl.findByLessonId(1L);
        assertSame(homeworkResponseDTOList, actualFindByLessonIdResult);
        assertTrue(actualFindByLessonIdResult.isEmpty());
        verify(homeworkRepository).findByLessonId(Mockito.<Long>any());
        verify(homeworkMapper).toResponseDto(Mockito.<List<Homework>>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#findByLessonId(Long)}
     */
    @Test
    void testFindByLessonId2() {
        when(homeworkRepository.findByLessonId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(homeworkMapper.toResponseDto(Mockito.<List<Homework>>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> homeworkServiceImpl.findByLessonId(1L));
        verify(homeworkRepository).findByLessonId(Mockito.<Long>any());
        verify(homeworkMapper).toResponseDto(Mockito.<List<Homework>>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#uploadHomeworkForLesson(Long, MultipartFile, Long)}
     */
    @Test
    void testUploadHomeworkForLesson() {
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
        Optional<Lesson> ofResult2 = Optional.of(lesson);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(SystemException.class, () -> homeworkServiceImpl.uploadHomeworkForLesson(1L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes(StandardCharsets.UTF_8))), 1L));
        verify(studentRepository).findById(Mockito.<Long>any());
        verify(lessonRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#uploadHomeworkForLesson(Long, MultipartFile, Long)}
     */
    @Test
    void testUploadHomeworkForLesson2() {
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
        when(lessonRepository.findById(Mockito.<Long>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> homeworkServiceImpl.uploadHomeworkForLesson(1L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes(StandardCharsets.UTF_8))), 1L));
        verify(studentRepository).findById(Mockito.<Long>any());
        verify(lessonRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#uploadHomeworkForLesson(Long, MultipartFile, Long)}
     */
    @Test
    void testUploadHomeworkForLesson3() throws IOException {
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

        Homework homework = new Homework();
        homework.setCreatedBy("Anonymous");
        homework.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setFilePath("/directory/foo.txt");
        homework.setId(1L);
        homework.setLesson(lesson);
        homework.setModifiedBy("Anonymous");
        homework.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setStudent(student);
        homework.setTitle("Dr");
        when(homeworkRepository.save(Mockito.<Homework>any())).thenReturn(homework);

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

        HashSet<Course> studentCourses = new HashSet<>();
        studentCourses.add(course2);

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
        student2.setStudentCourses(studentCourses);
        Optional<Student> ofResult = Optional.of(student2);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        course3.setStudents(new HashSet<>());
        course3.setTitle("Dr");

        Lesson lesson2 = new Lesson();
        lesson2.setCourse(course3);
        lesson2.setCreatedBy("Anonymous");
        lesson2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setDescription("The characteristics of someone or something");
        lesson2.setId(1L);
        lesson2.setModifiedBy("Anonymous");
        lesson2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setSubmissions(new HashSet<>());
        lesson2.setTitle("Dr");
        Optional<Lesson> ofResult2 = Optional.of(lesson2);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        HomeworkResponseDTO homeworkResponseDTO = new HomeworkResponseDTO();
        homeworkResponseDTO.setCreatedBy("Anonymous");
        homeworkResponseDTO.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homeworkResponseDTO.setFilePath("/directory/foo.txt");
        homeworkResponseDTO.setId(1L);
        homeworkResponseDTO.setLessonId(1L);
        homeworkResponseDTO.setModifiedBy("Anonymous");
        homeworkResponseDTO.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homeworkResponseDTO.setStudentId(1L);
        homeworkResponseDTO.setTitle("Dr");
        when(homeworkMapper.toResponseDto(Mockito.<Homework>any())).thenReturn(homeworkResponseDTO);
        assertSame(homeworkResponseDTO, homeworkServiceImpl.uploadHomeworkForLesson(1L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes(StandardCharsets.UTF_8))), 1L));
        verify(homeworkRepository).save(Mockito.<Homework>any());
        verify(studentRepository).findById(Mockito.<Long>any());
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(homeworkMapper).toResponseDto(Mockito.<Homework>any());
    }

    /**
     * Method under test: {@link HomeworkServiceImpl#uploadHomeworkForLesson(Long, MultipartFile, Long)}
     */
    @Test
    void testUploadHomeworkForLesson4() {
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

        Homework homework = new Homework();
        homework.setCreatedBy("Anonymous");
        homework.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setFilePath("/directory/foo.txt");
        homework.setId(1L);
        homework.setLesson(lesson);
        homework.setModifiedBy("Anonymous");
        homework.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        homework.setStudent(student);
        homework.setTitle("Dr");
        when(homeworkRepository.save(Mockito.<Homework>any())).thenReturn(homework);

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

        HashSet<Course> studentCourses = new HashSet<>();
        studentCourses.add(course2);

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
        student2.setStudentCourses(studentCourses);
        Optional<Student> ofResult = Optional.of(student2);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        course3.setStudents(new HashSet<>());
        course3.setTitle("Dr");

        Lesson lesson2 = new Lesson();
        lesson2.setCourse(course3);
        lesson2.setCreatedBy("Anonymous");
        lesson2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setDescription("The characteristics of someone or something");
        lesson2.setId(1L);
        lesson2.setModifiedBy("Anonymous");
        lesson2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setSubmissions(new HashSet<>());
        lesson2.setTitle("Dr");
        Optional<Lesson> ofResult2 = Optional.of(lesson2);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(homeworkMapper.toResponseDto(Mockito.<Homework>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> homeworkServiceImpl.uploadHomeworkForLesson(1L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes(StandardCharsets.UTF_8))), 1L));
        verify(homeworkRepository).save(Mockito.<Homework>any());
        verify(studentRepository).findById(Mockito.<Long>any());
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(homeworkMapper).toResponseDto(Mockito.<Homework>any());
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
                .thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> homeworkServiceImpl.findAllByStudentIdAndCourseId(1L, 1L));
        verify(homeworkRepository).findByStudentIdAndLessonId(Mockito.<Long>any(), Mockito.<Long>any());
    }
}

