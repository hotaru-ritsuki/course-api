package com.example.courseapi.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.generic.FiltersImpl;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.domain.Instructor;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.Submission;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.CourseStatus;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.CourseDTO;
import com.example.courseapi.dto.CourseGradeDTO;
import com.example.courseapi.dto.CourseStatusDTO;
import com.example.courseapi.exception.IllegalRoleAccessException;
import com.example.courseapi.exception.InstructorNotFoundException;
import com.example.courseapi.exception.StudentAlreadySubscribedToCourse;
import com.example.courseapi.exception.StudentNotSubscribedToCourse;
import com.example.courseapi.repository.CourseRepository;
import com.example.courseapi.repository.InstructorRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.SubmissionService;
import com.example.courseapi.service.mapper.CourseMapper;
import com.example.courseapi.service.mapper.CourseStatusMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CourseServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CourseServiceImplTest {
    @MockBean
    private CourseMapper courseMapper;

    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private CourseServiceImpl courseServiceImpl;

    @MockBean
    private CourseStatusMapper courseStatusMapper;

    @MockBean
    private InstructorRepository instructorRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private SubmissionService submissionService;

    @MockBean
    private UserRepository userRepository;

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
     * Method under test: {@link CourseServiceImpl#findById(Long)}
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCreatedBy("username");
        courseDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setDescription("The characteristics of someone or something");
        courseDTO.setId(1L);
        courseDTO.setInstructorIds(new HashSet<>());
        courseDTO.setLessonIds(new HashSet<>());
        courseDTO.setModifiedBy("username");
        courseDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setStudentIds(new HashSet<>());
        courseDTO.setTitle("Dr");
        when(courseMapper.toDto(Mockito.<Course>any())).thenReturn(courseDTO);
        assertTrue(courseServiceImpl.findById(1L).isPresent());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#save(CourseDTO)}
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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course);

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

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCreatedBy("username");
        courseDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setDescription("The characteristics of someone or something");
        courseDTO.setId(1L);
        courseDTO.setInstructorIds(new HashSet<>());
        courseDTO.setLessonIds(new HashSet<>());
        courseDTO.setModifiedBy("username");
        courseDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setStudentIds(new HashSet<>());
        courseDTO.setTitle("Dr");
        when(courseMapper.toEntity(Mockito.<CourseDTO>any())).thenReturn(course2);
        when(courseMapper.toDto(Mockito.<Course>any())).thenReturn(courseDTO);

        CourseDTO courseDTO2 = new CourseDTO();
        courseDTO2.setCreatedBy("username");
        courseDTO2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO2.setDescription("The characteristics of someone or something");
        courseDTO2.setId(1L);
        courseDTO2.setInstructorIds(new HashSet<>());
        courseDTO2.setLessonIds(new HashSet<>());
        courseDTO2.setModifiedBy("username");
        courseDTO2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO2.setStudentIds(new HashSet<>());
        courseDTO2.setTitle("Dr");
        assertSame(courseDTO, courseServiceImpl.save(courseDTO2));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseMapper).toEntity(Mockito.<CourseDTO>any());
        verify(courseMapper).toDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#save(CourseDTO)}
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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course);
        when(courseMapper.toEntity(Mockito.<CourseDTO>any())).thenThrow(new InstructorNotFoundException());
        when(courseMapper.toDto(Mockito.<Course>any())).thenThrow(new InstructorNotFoundException());

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCreatedBy("username");
        courseDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setDescription("The characteristics of someone or something");
        courseDTO.setId(1L);
        courseDTO.setInstructorIds(new HashSet<>());
        courseDTO.setLessonIds(new HashSet<>());
        courseDTO.setModifiedBy("username");
        courseDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setStudentIds(new HashSet<>());
        courseDTO.setTitle("Dr");
        assertThrows(InstructorNotFoundException.class, () -> courseServiceImpl.save(courseDTO));
        verify(courseMapper).toEntity(Mockito.<CourseDTO>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#findAll(Filters, Pageable)}
     */
    @Test
    void testFindAll() {
        when(courseRepository.findAll(Mockito.<Specification<Course>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(courseServiceImpl.findAll(new FiltersImpl(), null).toList().isEmpty());
        verify(courseRepository).findAll(Mockito.<Specification<Course>>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#findByStudentId(Long)}
     */
    @Test
    void testFindByStudentId() {
        when(courseRepository.findByStudentsId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<CourseDTO> courseDTOList = new ArrayList<>();
        when(courseMapper.toDto(Mockito.<List<Course>>any())).thenReturn(courseDTOList);
        List<CourseDTO> actualFindByStudentIdResult = courseServiceImpl.findByStudentId(1L);
        assertSame(courseDTOList, actualFindByStudentIdResult);
        assertTrue(actualFindByStudentIdResult.isEmpty());
        verify(courseRepository).findByStudentsId(Mockito.<Long>any());
        verify(courseMapper).toDto(Mockito.<List<Course>>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#findByStudentId(Long)}
     */
    @Test
    void testFindByStudentId2() {
        when(courseRepository.findByStudentsId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(courseMapper.toDto(Mockito.<List<Course>>any())).thenThrow(new StudentAlreadySubscribedToCourse());
        assertThrows(StudentAlreadySubscribedToCourse.class, () -> courseServiceImpl.findByStudentId(1L));
        verify(courseRepository).findByStudentsId(Mockito.<Long>any());
        verify(courseMapper).toDto(Mockito.<List<Course>>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#delete(Long)}
     */
    @Test
    void testDelete() {
        doNothing().when(courseRepository).deleteById(Mockito.<Long>any());
        courseServiceImpl.delete(1L);
        verify(courseRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#delete(Long)}
     */
    @Test
    void testDelete2() {
        doThrow(new StudentAlreadySubscribedToCourse()).when(courseRepository).deleteById(Mockito.<Long>any());
        assertThrows(StudentAlreadySubscribedToCourse.class, () -> courseServiceImpl.delete(1L));
        verify(courseRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#subscribeStudentToCourse(Long, Long)}
     */
    @Test
    void testSubscribeStudentToCourse() {
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        Optional<Student> ofResult2 = Optional.of(student);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        courseServiceImpl.subscribeStudentToCourse(1L, 1L);
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(studentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#subscribeStudentToCourse(Long, Long)}
     */
    @Test
    void testSubscribeStudentToCourse2() {
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.save(Mockito.<Course>any())).thenThrow(new StudentAlreadySubscribedToCourse());
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        Optional<Student> ofResult2 = Optional.of(student);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(StudentAlreadySubscribedToCourse.class, () -> courseServiceImpl.subscribeStudentToCourse(1L, 1L));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(studentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#subscribeStudentToCourse(Long, Long)}
     */
    @Test
    void testSubscribeStudentToCourse3() {
        Course course = mock(Course.class);
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).addStudent(Mockito.<Student>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        Optional<Student> ofResult2 = Optional.of(student);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        courseServiceImpl.subscribeStudentToCourse(1L, 1L);
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).addStudent(Mockito.<Student>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(studentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#subscribeStudentToCourse(Long, Long)}
     */
    @Test
    void testSubscribeStudentToCourse5() {
        Course course = mock(Course.class);
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).addStudent(Mockito.<Student>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Student student = mock(Student.class);
        when(student.getStudentCourses()).thenReturn(new HashSet<>());
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
//        doNothing().when(student).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(student).setStudentCourses(Mockito.<Set<Course>>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
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
        Optional<Student> ofResult2 = Optional.of(student);
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        courseServiceImpl.subscribeStudentToCourse(1L, 1L);
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).addStudent(Mockito.<Student>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(studentRepository).findById(Mockito.<Long>any());
        verify(student).getStudentCourses();
        verify(student).setCreatedBy(Mockito.<String>any());
        verify(student).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(student).setModifiedBy(Mockito.<String>any());
        verify(student).setModifiedDate(Mockito.<LocalDateTime>any());
//        verify(student).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(student).setStudentCourses(Mockito.<Set<Course>>any());
        verify(student).setEmail(Mockito.<String>any());
        verify(student).setFirstName(Mockito.<String>any());
        verify(student).setId(Mockito.<Long>any());
        verify(student).setLastName(Mockito.<String>any());
        verify(student).setPassword(Mockito.<String>any());
        verify(student).setRole(Mockito.<Roles>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getCourseStatus(Long, Long)}
     */
    @Test
    void testGetCourseStatus() {
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(true);

        CourseStatusDTO courseStatusDTO = new CourseStatusDTO();
        courseStatusDTO.setCourseStatus(CourseStatus.COMPLETED);
        courseStatusDTO.setCreatedBy("username");
        courseStatusDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setDescription("The characteristics of someone or something");
        courseStatusDTO.setFinalGrade(10.0d);
        courseStatusDTO.setId(1L);
        courseStatusDTO.setInstructorIds(new HashSet<>());
        courseStatusDTO.setLessonIds(new HashSet<>());
        courseStatusDTO.setModifiedBy("username");
        courseStatusDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setStudentIds(new HashSet<>());
        courseStatusDTO.setTitle("Dr");
        when(courseStatusMapper.toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertSame(courseStatusDTO, courseServiceImpl.getCourseStatus(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(courseStatusMapper).toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getCourseStatus(Long, Long)}
     */
    @Test
    void testGetCourseStatus2() {
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(true);

        CourseStatusDTO courseStatusDTO = new CourseStatusDTO();
        courseStatusDTO.setCourseStatus(CourseStatus.COMPLETED);
        courseStatusDTO.setCreatedBy("username");
        courseStatusDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setDescription("The characteristics of someone or something");
        courseStatusDTO.setFinalGrade(10.0d);
        courseStatusDTO.setId(1L);
        courseStatusDTO.setInstructorIds(new HashSet<>());
        courseStatusDTO.setLessonIds(new HashSet<>());
        courseStatusDTO.setModifiedBy("username");
        courseStatusDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setStudentIds(new HashSet<>());
        courseStatusDTO.setTitle("Dr");
        when(courseStatusMapper.toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenThrow(new StudentAlreadySubscribedToCourse());
        assertThrows(StudentAlreadySubscribedToCourse.class, () -> courseServiceImpl.getCourseStatus(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getCourseStatus(Long, Long)}
     */
    @Test
    void testGetCourseStatus3() {
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1L);
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(true);

        CourseStatusDTO courseStatusDTO = new CourseStatusDTO();
        courseStatusDTO.setCourseStatus(CourseStatus.COMPLETED);
        courseStatusDTO.setCreatedBy("username");
        courseStatusDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setDescription("The characteristics of someone or something");
        courseStatusDTO.setFinalGrade(10.0d);
        courseStatusDTO.setId(1L);
        courseStatusDTO.setInstructorIds(new HashSet<>());
        courseStatusDTO.setLessonIds(new HashSet<>());
        courseStatusDTO.setModifiedBy("username");
        courseStatusDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setStudentIds(new HashSet<>());
        courseStatusDTO.setTitle("Dr");
        when(courseStatusMapper.toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertSame(courseStatusDTO, courseServiceImpl.getCourseStatus(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).getId();
        verify(course).getLessons();
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(courseStatusMapper).toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getCourseStatus(Long, Long)}
     */
    @Test
    void testGetCourseStatus4() {
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

        HashSet<Lesson> lessonSet = new HashSet<>();
        lessonSet.add(lesson);
        Course course2 = mock(Course.class);
        when(course2.getId()).thenReturn(1L);
        when(course2.getLessons()).thenReturn(lessonSet);
        doNothing().when(course2).setCreatedBy(Mockito.<String>any());
        doNothing().when(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).setModifiedBy(Mockito.<String>any());
        doNothing().when(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course2).setDescription(Mockito.<String>any());
        doNothing().when(course2).setId(Mockito.<Long>any());
        doNothing().when(course2).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course2).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course2).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course2).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(true);

        CourseStatusDTO courseStatusDTO = new CourseStatusDTO();
        courseStatusDTO.setCourseStatus(CourseStatus.COMPLETED);
        courseStatusDTO.setCreatedBy("username");
        courseStatusDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setDescription("The characteristics of someone or something");
        courseStatusDTO.setFinalGrade(10.0d);
        courseStatusDTO.setId(1L);
        courseStatusDTO.setInstructorIds(new HashSet<>());
        courseStatusDTO.setLessonIds(new HashSet<>());
        courseStatusDTO.setModifiedBy("username");
        courseStatusDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setStudentIds(new HashSet<>());
        courseStatusDTO.setTitle("Dr");
        when(courseStatusMapper.toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertSame(courseStatusDTO, courseServiceImpl.getCourseStatus(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course2).getId();
        verify(course2).getLessons();
        verify(course2).setCreatedBy(Mockito.<String>any());
        verify(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course2).setModifiedBy(Mockito.<String>any());
        verify(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course2).setDescription(Mockito.<String>any());
        verify(course2).setId(Mockito.<Long>any());
        verify(course2).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course2).setLessons(Mockito.<Set<Lesson>>any());
        verify(course2).setStudents(Mockito.<Set<Student>>any());
        verify(course2).setTitle(Mockito.<String>any());
        verify(courseStatusMapper).toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getCourseStatus(Long, Long)}
     */
    @Test
    void testGetCourseStatus6() {
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1L);
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(false);

        CourseStatusDTO courseStatusDTO = new CourseStatusDTO();
        courseStatusDTO.setCourseStatus(CourseStatus.COMPLETED);
        courseStatusDTO.setCreatedBy("username");
        courseStatusDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setDescription("The characteristics of someone or something");
        courseStatusDTO.setFinalGrade(10.0d);
        courseStatusDTO.setId(1L);
        courseStatusDTO.setInstructorIds(new HashSet<>());
        courseStatusDTO.setLessonIds(new HashSet<>());
        courseStatusDTO.setModifiedBy("username");
        courseStatusDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setStudentIds(new HashSet<>());
        courseStatusDTO.setTitle("Dr");
        when(courseStatusMapper.toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertThrows(StudentNotSubscribedToCourse.class, () -> courseServiceImpl.getCourseStatus(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#calculateCourseStatus(Long, Course)}
     */
    @Test
    void testCalculateCourseStatus() {
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

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
        assertEquals(CourseStatus.FAILED, courseServiceImpl.calculateCourseStatus(1L, course).getCourseStatus());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#calculateCourseStatus(Long, Course)}
     */
    @Test
    void testCalculateCourseStatus2() {
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
        submission.setGrade(80.0d);
        submission.setLesson(lesson);
        submission.setModifiedBy("username");
        submission.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submission.setStudent(student);
        submission.setSubmissionId(submissionId);

        ArrayList<Submission> submissionList = new ArrayList<>();
        submissionList.add(submission);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(submissionList);

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
        assertEquals(CourseStatus.IN_PROGRESS, courseServiceImpl.calculateCourseStatus(1L, course2).getCourseStatus());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#calculateCourseStatus(Long, Course)}
     */
    @Test
    void testCalculateCourseStatus3() {
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1L);
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        assertEquals(CourseStatus.FAILED, courseServiceImpl.calculateCourseStatus(1L, course).getCourseStatus());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(course).getId();
        verify(course).getLessons();
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#calculateCourseStatus(Long, Set)}
     */
    @Test
    void testCalculateCourseStatus4() {
        assertTrue(courseServiceImpl.calculateCourseStatus(1L, new HashSet<>()).isEmpty());
    }

    /**
     * Method under test: {@link CourseServiceImpl#calculateCourseStatus(Long, Set)}
     */
    @Test
    void testCalculateCourseStatus5() {
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

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

        HashSet<Course> courses = new HashSet<>();
        courses.add(course);
        assertEquals(1, courseServiceImpl.calculateCourseStatus(1L, courses).size());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#calculateCourseStatus(Long, Set)}
     */
    @Test
    void testCalculateCourseStatus6() {
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
        submission.setGrade(80.0d);
        submission.setLesson(lesson);
        submission.setModifiedBy("username");
        submission.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        submission.setStudent(student);
        submission.setSubmissionId(submissionId);

        ArrayList<Submission> submissionList = new ArrayList<>();
        submissionList.add(submission);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(submissionList);

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

        HashSet<Course> courses = new HashSet<>();
        courses.add(course2);
        assertEquals(1, courseServiceImpl.calculateCourseStatus(1L, courses).size());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#calculateCourseStatus(Long, Set)}
     */
    @Test
    void testCalculateCourseStatus7() {
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());

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

        Course course2 = new Course();
        course2.setCourseFeedbacks(new HashSet<>());
        course2.setCreatedBy("Created By");
        course2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course2.setDescription("Description");
        course2.setId(2L);
        course2.setInstructors(new HashSet<>());
        course2.setLessons(new HashSet<>());
        course2.setModifiedBy("Modified By");
        course2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course2.setStudents(new HashSet<>());
        course2.setTitle("Mr");

        HashSet<Course> courses = new HashSet<>();
        courses.add(course2);
        courses.add(course);
        assertEquals(1, courseServiceImpl.calculateCourseStatus(1L, courses).size());
        verify(submissionService, atLeast(1)).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#calculateCourseStatus(Long, Set)}
     */
    @Test
    void testCalculateCourseStatus8() {
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1L);
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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

        HashSet<Course> courses = new HashSet<>();
        courses.add(course);
        assertEquals(1, courseServiceImpl.calculateCourseStatus(1L, courses).size());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(course).getId();
        verify(course).getLessons();
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#isStudentSubscribedToCourse(Long, Long)}
     */
    @Test
    void testIsStudentSubscribedToCourse() {
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(true);
        assertTrue(courseServiceImpl.isStudentSubscribedToCourse(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#isStudentSubscribedToCourse(Long, Long)}
     */
    @Test
    void testIsStudentSubscribedToCourse2() {
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(false);
        assertFalse(courseServiceImpl.isStudentSubscribedToCourse(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#isStudentSubscribedToCourse(Long, Long)}
     */
    @Test
    void testIsStudentSubscribedToCourse3() {
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenThrow(new StudentAlreadySubscribedToCourse());
        assertThrows(StudentAlreadySubscribedToCourse.class, () -> courseServiceImpl.isStudentSubscribedToCourse(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getMyCourses(Long)}
     */
    @Test
    void testGetMyCourses() {
        User user = new User();
        user.setCreatedBy("username");
        user.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setEmail("user@courseapi.com");
        user.setFirstName("User");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("username");
        user.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setPassword("iloveyou");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(IllegalRoleAccessException.class, () -> courseServiceImpl.getMyCourses(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getMyCourses(Long)}
     */
    @Test
    void testGetMyCourses2() {
        Student student = mock(Student.class);
        when(student.getRole()).thenReturn(Roles.INSTRUCTOR);
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
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
        Optional<User> ofResult = Optional.of(student);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(IllegalRoleAccessException.class, () -> courseServiceImpl.getMyCourses(1L));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(student, atLeast(1)).getRole();
        verify(student).setCreatedBy(Mockito.<String>any());
        verify(student).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(student).setModifiedBy(Mockito.<String>any());
        verify(student).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(student).setEmail(Mockito.<String>any());
        verify(student).setFirstName(Mockito.<String>any());
        verify(student).setId(Mockito.<Long>any());
        verify(student).setLastName(Mockito.<String>any());
        verify(student).setPassword(Mockito.<String>any());
        verify(student).setRole(Mockito.<Roles>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getMyCourses(Long)}
     */
    @Test
    void testGetMyCourses3() {
        Student student = mock(Student.class);
        when(student.getStudentCourses()).thenReturn(new HashSet<>());
        when(student.getRole()).thenReturn(Roles.STUDENT);
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
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
        Optional<User> ofResult = Optional.of(student);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertTrue(courseServiceImpl.getMyCourses(1L).isEmpty());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(student).getRole();
        verify(student).getStudentCourses();
        verify(student).setCreatedBy(Mockito.<String>any());
        verify(student).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(student).setModifiedBy(Mockito.<String>any());
        verify(student).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(student).setEmail(Mockito.<String>any());
        verify(student).setFirstName(Mockito.<String>any());
        verify(student).setId(Mockito.<Long>any());
        verify(student).setLastName(Mockito.<String>any());
        verify(student).setPassword(Mockito.<String>any());
        verify(student).setRole(Mockito.<Roles>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getMyCourses(Long)}
     */
    @Test
    void testGetMyCourses4() {
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

        HashSet<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        Student student = mock(Student.class);
        when(student.getId()).thenReturn(1L);
        when(student.getStudentCourses()).thenReturn(courseSet);
        when(student.getRole()).thenReturn(Roles.STUDENT);
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
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
        Optional<User> ofResult = Optional.of(student);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        CourseStatusDTO courseStatusDTO = new CourseStatusDTO();
        courseStatusDTO.setCourseStatus(CourseStatus.COMPLETED);
        courseStatusDTO.setCreatedBy("username");
        courseStatusDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setDescription("The characteristics of someone or something");
        courseStatusDTO.setFinalGrade(10.0d);
        courseStatusDTO.setId(1L);
        courseStatusDTO.setInstructorIds(new HashSet<>());
        courseStatusDTO.setLessonIds(new HashSet<>());
        courseStatusDTO.setModifiedBy("username");
        courseStatusDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setStudentIds(new HashSet<>());
        courseStatusDTO.setTitle("Dr");
        when(courseStatusMapper.toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertEquals(1, courseServiceImpl.getMyCourses(1L).size());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(student).getRole();
        verify(student, atLeast(1)).getId();
        verify(student).getStudentCourses();
        verify(student).setCreatedBy(Mockito.<String>any());
        verify(student).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(student).setModifiedBy(Mockito.<String>any());
        verify(student).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(student).setEmail(Mockito.<String>any());
        verify(student).setFirstName(Mockito.<String>any());
        verify(student).setId(Mockito.<Long>any());
        verify(student).setLastName(Mockito.<String>any());
        verify(student).setPassword(Mockito.<String>any());
        verify(student).setRole(Mockito.<Roles>any());
        verify(courseStatusMapper).toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getMyCourses(Long)}
     */
    @Test
    void testGetMyCourses5() {
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

        HashSet<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        Student student = mock(Student.class);
        when(student.getId()).thenReturn(1L);
        when(student.getStudentCourses()).thenReturn(courseSet);
        when(student.getRole()).thenReturn(Roles.STUDENT);
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
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
        Optional<User> ofResult = Optional.of(student);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        CourseStatusDTO courseStatusDTO = new CourseStatusDTO();
        courseStatusDTO.setCourseStatus(CourseStatus.COMPLETED);
        courseStatusDTO.setCreatedBy("username");
        courseStatusDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setDescription("The characteristics of someone or something");
        courseStatusDTO.setFinalGrade(10.0d);
        courseStatusDTO.setId(1L);
        courseStatusDTO.setInstructorIds(new HashSet<>());
        courseStatusDTO.setLessonIds(new HashSet<>());
        courseStatusDTO.setModifiedBy("username");
        courseStatusDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setStudentIds(new HashSet<>());
        courseStatusDTO.setTitle("Dr");
        when(courseStatusMapper.toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenThrow(new StudentAlreadySubscribedToCourse());
        assertThrows(StudentAlreadySubscribedToCourse.class, () -> courseServiceImpl.getMyCourses(1L));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(student).getRole();
        verify(student).getId();
        verify(student).getStudentCourses();
        verify(student).setCreatedBy(Mockito.<String>any());
        verify(student).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(student).setModifiedBy(Mockito.<String>any());
        verify(student).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(student).setEmail(Mockito.<String>any());
        verify(student).setFirstName(Mockito.<String>any());
        verify(student).setId(Mockito.<Long>any());
        verify(student).setLastName(Mockito.<String>any());
        verify(student).setPassword(Mockito.<String>any());
        verify(student).setRole(Mockito.<Roles>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getMyCourses(Long)}
     */
    @Test
    void testGetMyCourses6() {
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

        Course course2 = new Course();
        course2.setCourseFeedbacks(new HashSet<>());
        course2.setCreatedBy("Created By");
        course2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course2.setDescription("Description");
        course2.setId(2L);
        course2.setInstructors(new HashSet<>());
        course2.setLessons(new HashSet<>());
        course2.setModifiedBy("Modified By");
        course2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        course2.setStudents(new HashSet<>());
        course2.setTitle("Mr");

        HashSet<Course> courseSet = new HashSet<>();
        courseSet.add(course2);
        courseSet.add(course);
        Student student = mock(Student.class);
        when(student.getId()).thenReturn(1L);
        when(student.getStudentCourses()).thenReturn(courseSet);
        when(student.getRole()).thenReturn(Roles.STUDENT);
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
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
        Optional<User> ofResult = Optional.of(student);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        CourseStatusDTO courseStatusDTO = new CourseStatusDTO();
        courseStatusDTO.setCourseStatus(CourseStatus.COMPLETED);
        courseStatusDTO.setCreatedBy("username");
        courseStatusDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setDescription("The characteristics of someone or something");
        courseStatusDTO.setFinalGrade(10.0d);
        courseStatusDTO.setId(1L);
        courseStatusDTO.setInstructorIds(new HashSet<>());
        courseStatusDTO.setLessonIds(new HashSet<>());
        courseStatusDTO.setModifiedBy("username");
        courseStatusDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setStudentIds(new HashSet<>());
        courseStatusDTO.setTitle("Dr");
        when(courseStatusMapper.toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertEquals(1, courseServiceImpl.getMyCourses(1L).size());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(student).getRole();
        verify(student, atLeast(1)).getId();
        verify(student).getStudentCourses();
        verify(student).setCreatedBy(Mockito.<String>any());
        verify(student).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(student).setModifiedBy(Mockito.<String>any());
        verify(student).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(student).setEmail(Mockito.<String>any());
        verify(student).setFirstName(Mockito.<String>any());
        verify(student).setId(Mockito.<Long>any());
        verify(student).setLastName(Mockito.<String>any());
        verify(student).setPassword(Mockito.<String>any());
        verify(student).setRole(Mockito.<Roles>any());
        verify(courseStatusMapper, atLeast(1)).toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any());
        verify(submissionService, atLeast(1)).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getMyCourses(Long)}
     */
    @Test
    void testGetMyCourses7() {
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1L);
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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

        HashSet<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        Student student = mock(Student.class);
        when(student.getId()).thenReturn(1L);
        when(student.getStudentCourses()).thenReturn(courseSet);
        when(student.getRole()).thenReturn(Roles.STUDENT);
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
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
        Optional<User> ofResult = Optional.of(student);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        CourseStatusDTO courseStatusDTO = new CourseStatusDTO();
        courseStatusDTO.setCourseStatus(CourseStatus.COMPLETED);
        courseStatusDTO.setCreatedBy("username");
        courseStatusDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setDescription("The characteristics of someone or something");
        courseStatusDTO.setFinalGrade(10.0d);
        courseStatusDTO.setId(1L);
        courseStatusDTO.setInstructorIds(new HashSet<>());
        courseStatusDTO.setLessonIds(new HashSet<>());
        courseStatusDTO.setModifiedBy("username");
        courseStatusDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setStudentIds(new HashSet<>());
        courseStatusDTO.setTitle("Dr");
        when(courseStatusMapper.toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertEquals(1, courseServiceImpl.getMyCourses(1L).size());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(student).getRole();
        verify(student, atLeast(1)).getId();
        verify(student).getStudentCourses();
        verify(student).setCreatedBy(Mockito.<String>any());
        verify(student).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(student).setModifiedBy(Mockito.<String>any());
        verify(student).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(student).setEmail(Mockito.<String>any());
        verify(student).setFirstName(Mockito.<String>any());
        verify(student).setId(Mockito.<Long>any());
        verify(student).setLastName(Mockito.<String>any());
        verify(student).setPassword(Mockito.<String>any());
        verify(student).setRole(Mockito.<Roles>any());
        verify(course).getId();
        verify(course).getLessons();
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(courseStatusMapper).toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getMyCourses(Long)}
     */
    @Test
    void testGetMyCourses8() {
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

        HashSet<Lesson> lessonSet = new HashSet<>();
        lessonSet.add(lesson);
        Course course2 = mock(Course.class);
        when(course2.getId()).thenReturn(1L);
        when(course2.getLessons()).thenReturn(lessonSet);
        doNothing().when(course2).setCreatedBy(Mockito.<String>any());
        doNothing().when(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).setModifiedBy(Mockito.<String>any());
        doNothing().when(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course2).setDescription(Mockito.<String>any());
        doNothing().when(course2).setId(Mockito.<Long>any());
        doNothing().when(course2).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course2).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course2).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course2).setTitle(Mockito.<String>any());
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

        HashSet<Course> courseSet = new HashSet<>();
        courseSet.add(course2);
        Student student = mock(Student.class);
        when(student.getId()).thenReturn(1L);
        when(student.getStudentCourses()).thenReturn(courseSet);
        when(student.getRole()).thenReturn(Roles.STUDENT);
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
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
        Optional<User> ofResult = Optional.of(student);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        CourseStatusDTO courseStatusDTO = new CourseStatusDTO();
        courseStatusDTO.setCourseStatus(CourseStatus.COMPLETED);
        courseStatusDTO.setCreatedBy("username");
        courseStatusDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setDescription("The characteristics of someone or something");
        courseStatusDTO.setFinalGrade(10.0d);
        courseStatusDTO.setId(1L);
        courseStatusDTO.setInstructorIds(new HashSet<>());
        courseStatusDTO.setLessonIds(new HashSet<>());
        courseStatusDTO.setModifiedBy("username");
        courseStatusDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseStatusDTO.setStudentIds(new HashSet<>());
        courseStatusDTO.setTitle("Dr");
        when(courseStatusMapper.toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertEquals(1, courseServiceImpl.getMyCourses(1L).size());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(student).getRole();
        verify(student, atLeast(1)).getId();
        verify(student).getStudentCourses();
        verify(student).setCreatedBy(Mockito.<String>any());
        verify(student).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(student).setModifiedBy(Mockito.<String>any());
        verify(student).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(student).setEmail(Mockito.<String>any());
        verify(student).setFirstName(Mockito.<String>any());
        verify(student).setId(Mockito.<Long>any());
        verify(student).setLastName(Mockito.<String>any());
        verify(student).setPassword(Mockito.<String>any());
        verify(student).setRole(Mockito.<Roles>any());
        verify(course2).getId();
        verify(course2).getLessons();
        verify(course2).setCreatedBy(Mockito.<String>any());
        verify(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course2).setModifiedBy(Mockito.<String>any());
        verify(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course2).setDescription(Mockito.<String>any());
        verify(course2).setId(Mockito.<Long>any());
        verify(course2).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course2).setLessons(Mockito.<Set<Lesson>>any());
        verify(course2).setStudents(Mockito.<Set<Student>>any());
        verify(course2).setTitle(Mockito.<String>any());
        verify(courseStatusMapper).toDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#addInstructorToCourse(Long, Long)}
     */
    @Test
    void testAddInstructorToCourse() {
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Instructor instructor = new Instructor();
        instructor.setCreatedBy("username");
        instructor.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        instructor.setEmail("user@courseapi.com");
        instructor.setFirstName("User");
        instructor.setId(1L);
        instructor.setInstructorCourses(new HashSet<>());
        instructor.setLastName("LastName");
        instructor.setModifiedBy("username");
        instructor.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        instructor.setPassword("iloveyou");
        instructor.setRole(Roles.ADMIN);
        Optional<Instructor> ofResult2 = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCreatedBy("username");
        courseDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setDescription("The characteristics of someone or something");
        courseDTO.setId(1L);
        courseDTO.setInstructorIds(new HashSet<>());
        courseDTO.setLessonIds(new HashSet<>());
        courseDTO.setModifiedBy("username");
        courseDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setStudentIds(new HashSet<>());
        courseDTO.setTitle("Dr");
        when(courseMapper.toDto(Mockito.<Course>any())).thenReturn(courseDTO);
        courseServiceImpl.addInstructorToCourse(1L, 1L);
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(instructorRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#addInstructorToCourse(Long, Long)}
     */
    @Test
    void testAddInstructorToCourse2() {
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Instructor instructor = new Instructor();
        instructor.setCreatedBy("username");
        instructor.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        instructor.setEmail("user@courseapi.com");
        instructor.setFirstName("User");
        instructor.setId(1L);
        instructor.setInstructorCourses(new HashSet<>());
        instructor.setLastName("LastName");
        instructor.setModifiedBy("username");
        instructor.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        instructor.setPassword("iloveyou");
        instructor.setRole(Roles.ADMIN);
        Optional<Instructor> ofResult2 = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(courseMapper.toDto(Mockito.<Course>any())).thenThrow(new StudentAlreadySubscribedToCourse());
        assertThrows(StudentAlreadySubscribedToCourse.class, () -> courseServiceImpl.addInstructorToCourse(1L, 1L));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(instructorRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#addInstructorToCourse(Long, Long)}
     */
    @Test
    void testAddInstructorToCourse3() {
        Course course = mock(Course.class);
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).addInstructor(Mockito.<Instructor>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Instructor instructor = new Instructor();
        instructor.setCreatedBy("username");
        instructor.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        instructor.setEmail("user@courseapi.com");
        instructor.setFirstName("User");
        instructor.setId(1L);
        instructor.setInstructorCourses(new HashSet<>());
        instructor.setLastName("LastName");
        instructor.setModifiedBy("username");
        instructor.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        instructor.setPassword("iloveyou");
        instructor.setRole(Roles.ADMIN);
        Optional<Instructor> ofResult2 = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCreatedBy("username");
        courseDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setDescription("The characteristics of someone or something");
        courseDTO.setId(1L);
        courseDTO.setInstructorIds(new HashSet<>());
        courseDTO.setLessonIds(new HashSet<>());
        courseDTO.setModifiedBy("username");
        courseDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setStudentIds(new HashSet<>());
        courseDTO.setTitle("Dr");
        when(courseMapper.toDto(Mockito.<Course>any())).thenReturn(courseDTO);
        courseServiceImpl.addInstructorToCourse(1L, 1L);
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).addInstructor(Mockito.<Instructor>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(instructorRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#deleteInstructorForCourse(Long, Long)}
     */
    @Test
    void testDeleteInstructorForCourse() {
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Instructor instructor = new Instructor();
        instructor.setCreatedBy("username");
        instructor.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        instructor.setEmail("user@courseapi.com");
        instructor.setFirstName("User");
        instructor.setId(1L);
        instructor.setInstructorCourses(new HashSet<>());
        instructor.setLastName("LastName");
        instructor.setModifiedBy("username");
        instructor.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        instructor.setPassword("iloveyou");
        instructor.setRole(Roles.ADMIN);
        Optional<Instructor> ofResult2 = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCreatedBy("username");
        courseDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setDescription("The characteristics of someone or something");
        courseDTO.setId(1L);
        courseDTO.setInstructorIds(new HashSet<>());
        courseDTO.setLessonIds(new HashSet<>());
        courseDTO.setModifiedBy("username");
        courseDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setStudentIds(new HashSet<>());
        courseDTO.setTitle("Dr");
        when(courseMapper.toDto(Mockito.<Course>any())).thenReturn(courseDTO);
        courseServiceImpl.deleteInstructorForCourse(1L, 1L);
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(instructorRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#deleteInstructorForCourse(Long, Long)}
     */
    @Test
    void testDeleteInstructorForCourse2() {
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Instructor instructor = new Instructor();
        instructor.setCreatedBy("username");
        instructor.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        instructor.setEmail("user@courseapi.com");
        instructor.setFirstName("User");
        instructor.setId(1L);
        instructor.setInstructorCourses(new HashSet<>());
        instructor.setLastName("LastName");
        instructor.setModifiedBy("username");
        instructor.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        instructor.setPassword("iloveyou");
        instructor.setRole(Roles.ADMIN);
        Optional<Instructor> ofResult2 = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(courseMapper.toDto(Mockito.<Course>any())).thenThrow(new StudentAlreadySubscribedToCourse());
        assertThrows(StudentAlreadySubscribedToCourse.class, () -> courseServiceImpl.deleteInstructorForCourse(1L, 1L));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(instructorRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#deleteInstructorForCourse(Long, Long)}
     */
    @Test
    void testDeleteInstructorForCourse3() {
        Course course = mock(Course.class);
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).removeInstructor(Mockito.<Instructor>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Instructor instructor = new Instructor();
        instructor.setCreatedBy("username");
        instructor.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        instructor.setEmail("user@courseapi.com");
        instructor.setFirstName("User");
        instructor.setId(1L);
        instructor.setInstructorCourses(new HashSet<>());
        instructor.setLastName("LastName");
        instructor.setModifiedBy("username");
        instructor.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        instructor.setPassword("iloveyou");
        instructor.setRole(Roles.ADMIN);
        Optional<Instructor> ofResult2 = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCreatedBy("username");
        courseDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setDescription("The characteristics of someone or something");
        courseDTO.setId(1L);
        courseDTO.setInstructorIds(new HashSet<>());
        courseDTO.setLessonIds(new HashSet<>());
        courseDTO.setModifiedBy("username");
        courseDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setStudentIds(new HashSet<>());
        courseDTO.setTitle("Dr");
        when(courseMapper.toDto(Mockito.<Course>any())).thenReturn(courseDTO);
        courseServiceImpl.deleteInstructorForCourse(1L, 1L);
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).removeInstructor(Mockito.<Instructor>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(instructorRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toDto(Mockito.<Course>any());
    }
}

