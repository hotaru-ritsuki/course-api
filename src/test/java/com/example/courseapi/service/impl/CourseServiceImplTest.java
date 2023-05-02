package com.example.courseapi.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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
import com.example.courseapi.dto.CourseGradeDTO;
import com.example.courseapi.dto.request.CourseRequestDTO;
import com.example.courseapi.dto.request.LessonRequestDTO;
import com.example.courseapi.dto.request.LessonsUpdateDTO;
import com.example.courseapi.dto.response.CourseResponseDTO;
import com.example.courseapi.dto.response.CourseStatusResponseDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.CourseRepository;
import com.example.courseapi.repository.InstructorRepository;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.SubmissionService;
import com.example.courseapi.service.mapper.CourseMapper;
import com.example.courseapi.service.mapper.LessonMapper;

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
    private InstructorRepository instructorRepository;

    @MockBean
    private LessonMapper lessonMapper;

    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private SubmissionService submissionService;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link CourseServiceImpl#findById(Long)}
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(new CourseResponseDTO());
        assertTrue(courseServiceImpl.findById(1L).isPresent());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toResponseDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#findById(Long)}
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> courseServiceImpl.findById(1L));
    }

    /**
     * Method under test: {@link CourseServiceImpl#save(CourseRequestDTO)}
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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course);

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
        when(courseMapper.fromRequestDto(Mockito.<CourseRequestDTO>any())).thenReturn(course2);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(courseResponseDTO);
        assertSame(courseResponseDTO, courseServiceImpl.save(new CourseRequestDTO()));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseMapper).fromRequestDto(Mockito.<CourseRequestDTO>any());
        verify(courseMapper).toResponseDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#save(CourseRequestDTO)}
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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course);
        when(courseMapper.fromRequestDto(Mockito.<CourseRequestDTO>any())).thenThrow(new SystemException(ErrorCode.OK));
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> courseServiceImpl.save(new CourseRequestDTO()));
        verify(courseMapper).fromRequestDto(Mockito.<CourseRequestDTO>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#save(CourseRequestDTO)}
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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course);

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
        when(courseMapper.fromRequestDto(Mockito.<CourseRequestDTO>any())).thenReturn(course2);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(courseResponseDTO);
        HashSet<Long> instructorIds = new HashSet<>();
        assertSame(courseResponseDTO, courseServiceImpl.save(new CourseRequestDTO(1L, "Dr",
                "The characteristics of someone or something", instructorIds, new HashSet<>())));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseMapper).fromRequestDto(Mockito.<CourseRequestDTO>any());
        verify(courseMapper).toResponseDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#save(CourseRequestDTO)}
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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course);

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
        when(courseMapper.fromRequestDto(Mockito.<CourseRequestDTO>any())).thenReturn(course2);
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(new CourseResponseDTO());
        courseServiceImpl.save(new CourseRequestDTO());
    }

    /**
     * Method under test: {@link CourseServiceImpl#findAll(Filters, Pageable, User)}
     */
    @Test
    void testFindAll() {
        when(courseRepository.findAll(Mockito.<Specification<Course>>any(), Mockito.<Pageable>any()))
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
        assertTrue(courseServiceImpl.findAll(filters, null, user).toList().isEmpty());
        verify(courseRepository).findAll(Mockito.<Specification<Course>>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#findByStudentId(Long)}
     */
    @Test
    void testFindByStudentId() {
        when(courseRepository.findByStudentsId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<CourseResponseDTO> courseResponseDTOList = new ArrayList<>();
        when(courseMapper.toResponseDto(Mockito.<List<Course>>any())).thenReturn(courseResponseDTOList);
        List<CourseResponseDTO> actualFindByStudentIdResult = courseServiceImpl.findByStudentId(1L);
        assertSame(courseResponseDTOList, actualFindByStudentIdResult);
        assertTrue(actualFindByStudentIdResult.isEmpty());
        verify(courseRepository).findByStudentsId(Mockito.<Long>any());
        verify(courseMapper).toResponseDto(Mockito.<List<Course>>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#findByStudentId(Long)}
     */
    @Test
    void testFindByStudentId2() {
        when(courseRepository.findByStudentsId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(courseMapper.toResponseDto(Mockito.<List<Course>>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> courseServiceImpl.findByStudentId(1L));
        verify(courseRepository).findByStudentsId(Mockito.<Long>any());
        verify(courseMapper).toResponseDto(Mockito.<List<Course>>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#delete(Long)}
     */
    @Test
    void testDelete() {
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
        Optional<Course> ofResult = Optional.of(course);
        doNothing().when(courseRepository).delete(Mockito.<Course>any());
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        courseServiceImpl.delete(1L);
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(courseRepository).delete(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#delete(Long)}
     */
    @Test
    void testDelete2() {
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
        Optional<Course> ofResult = Optional.of(course);
        doThrow(new SystemException(ErrorCode.OK)).when(courseRepository).delete(Mockito.<Course>any());
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(SystemException.class, () -> courseServiceImpl.delete(1L));
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(courseRepository).delete(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#delete(Long)}
     */
    @Test
    void testDelete3() {
        doNothing().when(courseRepository).delete(Mockito.<Course>any());
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(SystemException.class, () -> courseServiceImpl.delete(1L));
        verify(courseRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#subscribeStudentToCourse(Long, Long)}
     */
    @Test
    void testSubscribeStudentToCourse() {
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.save(Mockito.<Course>any())).thenThrow(new SystemException(ErrorCode.OK));
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        assertThrows(SystemException.class, () -> courseServiceImpl.subscribeStudentToCourse(1L, 1L));
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
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        when(course.getAvailable()).thenReturn(true);
        courseServiceImpl.subscribeStudentToCourse(1L, 1L);
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).addStudent(Mockito.<Student>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
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
    void testSubscribeStudentToCourse4() {
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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        Course course2 = mock(Course.class);
        doNothing().when(course2).setCreatedBy(Mockito.<String>any());
        doNothing().when(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).setModifiedBy(Mockito.<String>any());
        doNothing().when(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).addStudent(Mockito.<Student>any());
        doNothing().when(course2).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course2).setDescription(Mockito.<String>any());
        doNothing().when(course2).setId(Mockito.<Long>any());
        doNothing().when(course2).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course2).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course2).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course2).setTitle(Mockito.<String>any());
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
        assertThrows(SystemException.class, () -> courseServiceImpl.subscribeStudentToCourse(1L, 1L));
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course2).setCreatedBy(Mockito.<String>any());
        verify(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course2).setModifiedBy(Mockito.<String>any());
        verify(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course2).setAvailable(Mockito.<Boolean>any());
        verify(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course2).setDescription(Mockito.<String>any());
        verify(course2).setId(Mockito.<Long>any());
        verify(course2).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course2).setLessons(Mockito.<Set<Lesson>>any());
        verify(course2).setStudents(Mockito.<Set<Student>>any());
        verify(course2).setTitle(Mockito.<String>any());
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
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Student student = mock(Student.class);
        when(student.getStudentCourses()).thenReturn(new HashSet<>());
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setStudentCourses(Mockito.<Set<Course>>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
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
        when(course.getAvailable()).thenReturn(true);
        courseServiceImpl.subscribeStudentToCourse(1L, 1L);
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).addStudent(Mockito.<Student>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(true);
        CourseStatusResponseDTO courseStatusResponseDTO = new CourseStatusResponseDTO();
        when(courseMapper.toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusResponseDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertSame(courseStatusResponseDTO, courseServiceImpl.getCourseStatus(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(),
                Mockito.<CourseGradeDTO>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getCourseStatus(Long, Long)}
     */
    @Test
    void testGetCourseStatus2() {
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(true);
        when(courseMapper.toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(new CourseStatusResponseDTO());
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> courseServiceImpl.getCourseStatus(1L, 1L));
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
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(true);
        CourseStatusResponseDTO courseStatusResponseDTO = new CourseStatusResponseDTO();
        when(courseMapper.toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusResponseDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertSame(courseStatusResponseDTO, courseServiceImpl.getCourseStatus(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course, times(5)).getId();
        verify(course).getLessons();
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(courseMapper).toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(),
                Mockito.<CourseGradeDTO>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getCourseStatus(Long, Long)}
     */
    @Test
    void testGetCourseStatus4() {
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

        HashSet<Lesson> lessonSet = new HashSet<>();
        lessonSet.add(lesson);
        Course course2 = mock(Course.class);
        when(course2.getId()).thenReturn(1L);
        when(course2.getLessons()).thenReturn(lessonSet);
        doNothing().when(course2).setCreatedBy(Mockito.<String>any());
        doNothing().when(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).setModifiedBy(Mockito.<String>any());
        doNothing().when(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course2).setDescription(Mockito.<String>any());
        doNothing().when(course2).setId(Mockito.<Long>any());
        doNothing().when(course2).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course2).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course2).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course2).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(true);
        CourseStatusResponseDTO courseStatusResponseDTO = new CourseStatusResponseDTO();
        when(courseMapper.toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(courseStatusResponseDTO);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertSame(courseStatusResponseDTO, courseServiceImpl.getCourseStatus(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course2, times(5)).getId();
        verify(course2).getLessons();
        verify(course2).setCreatedBy(Mockito.<String>any());
        verify(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course2).setModifiedBy(Mockito.<String>any());
        verify(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course2).setAvailable(Mockito.<Boolean>any());
        verify(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course2).setDescription(Mockito.<String>any());
        verify(course2).setId(Mockito.<Long>any());
        verify(course2).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course2).setLessons(Mockito.<Set<Lesson>>any());
        verify(course2).setStudents(Mockito.<Set<Student>>any());
        verify(course2).setTitle(Mockito.<String>any());
        verify(courseMapper).toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(),
                Mockito.<CourseGradeDTO>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getCourseStatus(Long, Long)}
     */
    @Test
    void testGetCourseStatus5() {
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(true);
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1L);
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        when(courseMapper.toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(new CourseStatusResponseDTO());
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertThrows(SystemException.class, () -> courseServiceImpl.getCourseStatus(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
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
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseRepository.existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(false);
        when(courseMapper.toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(new CourseStatusResponseDTO());
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertThrows(SystemException.class, () -> courseServiceImpl.getCourseStatus(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
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
        assertEquals(CourseStatus.FAILED, courseServiceImpl.calculateCourseStatus(1L, course).getCourseStatus());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#calculateCourseStatus(Long, Course)}
     */
    @Test
    void testCalculateCourseStatus2() {
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
        submission.setGrade(80.0d);
        submission.setLesson(lesson);
        submission.setModifiedBy("Anonymous");
        submission.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission.setStudent(student);
        submission.setSubmissionId(submissionId);

        ArrayList<Submission> submissionList = new ArrayList<>();
        submissionList.add(submission);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(submissionList);

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
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        assertEquals(CourseStatus.FAILED, courseServiceImpl.calculateCourseStatus(1L, course).getCourseStatus());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(course, times(5)).getId();
        verify(course).getLessons();
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
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
        submission.setGrade(80.0d);
        submission.setLesson(lesson);
        submission.setModifiedBy("Anonymous");
        submission.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        submission.setStudent(student);
        submission.setSubmissionId(submissionId);

        ArrayList<Submission> submissionList = new ArrayList<>();
        submissionList.add(submission);
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(submissionList);

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
        course2.setAvailable(false);
        course2.setCourseFeedbacks(new HashSet<>());
        course2.setCreatedBy("Created By");
        course2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course2.setDescription("Description");
        course2.setId(2L);
        course2.setInstructors(new HashSet<>());
        course2.setLessons(new HashSet<>());
        course2.setModifiedBy("Modified By");
        course2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
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
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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

        HashSet<Course> courses = new HashSet<>();
        courses.add(course);
        assertEquals(1, courseServiceImpl.calculateCourseStatus(1L, courses).size());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(course, times(6)).getId();
        verify(course).getLessons();
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course, times(1)).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
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
                .thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> courseServiceImpl.isStudentSubscribedToCourse(1L, 1L));
        verify(courseRepository).existsByIdAndStudentsId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getMyCourses(Long)}
     */
    @Test
    void testGetMyCourses() {
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
        assertThrows(SystemException.class, () -> courseServiceImpl.getMyCourses(1L));
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
        assertThrows(SystemException.class, () -> courseServiceImpl.getMyCourses(1L));
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
        when(courseMapper.toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(new CourseStatusResponseDTO());
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
        verify(courseMapper).toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(),
                Mockito.<CourseGradeDTO>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getMyCourses(Long)}
     */
    @Test
    void testGetMyCourses5() {
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
        when(courseMapper.toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(new CourseStatusResponseDTO());
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> courseServiceImpl.getMyCourses(1L));
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
        course2.setAvailable(false);
        course2.setCourseFeedbacks(new HashSet<>());
        course2.setCreatedBy("Created By");
        course2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        course2.setDescription("Description");
        course2.setId(2L);
        course2.setInstructors(new HashSet<>());
        course2.setLessons(new HashSet<>());
        course2.setModifiedBy("Modified By");
        course2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
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
        when(courseMapper.toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(new CourseStatusResponseDTO());
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
        verify(courseMapper, atLeast(1)).toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(),
                Mockito.<CourseGradeDTO>any());
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
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        when(courseMapper.toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(new CourseStatusResponseDTO());
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
        verify(course, times(5)).getId();
        verify(course).getLessons();
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(courseMapper).toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(),
                Mockito.<CourseGradeDTO>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getMyCourses(Long)}
     */
    @Test
    void testGetMyCourses8() {
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

        HashSet<Lesson> lessonSet = new HashSet<>();
        lessonSet.add(lesson);
        Course course2 = mock(Course.class);
        when(course2.getId()).thenReturn(1L);
        when(course2.getLessons()).thenReturn(lessonSet);
        doNothing().when(course2).setCreatedBy(Mockito.<String>any());
        doNothing().when(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).setModifiedBy(Mockito.<String>any());
        doNothing().when(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course2).setDescription(Mockito.<String>any());
        doNothing().when(course2).setId(Mockito.<Long>any());
        doNothing().when(course2).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course2).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course2).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course2).setTitle(Mockito.<String>any());
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
        when(courseMapper.toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(new CourseStatusResponseDTO());
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
        verify(course2, times(5)).getId();
        verify(course2).getLessons();
        verify(course2).setCreatedBy(Mockito.<String>any());
        verify(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course2).setModifiedBy(Mockito.<String>any());
        verify(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course2).setAvailable(Mockito.<Boolean>any());
        verify(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course2).setDescription(Mockito.<String>any());
        verify(course2).setId(Mockito.<Long>any());
        verify(course2).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course2).setLessons(Mockito.<Set<Lesson>>any());
        verify(course2).setStudents(Mockito.<Set<Student>>any());
        verify(course2).setTitle(Mockito.<String>any());
        verify(courseMapper).toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(),
                Mockito.<CourseGradeDTO>any());
        verify(submissionService).findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#getMyCourses(Long)}
     */
    @Test
    void testGetMyCourses9() {
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1L);
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        when(courseMapper.toResponseStatusDto(Mockito.<Course>any(), Mockito.<Long>any(), Mockito.<CourseGradeDTO>any()))
                .thenReturn(new CourseStatusResponseDTO());
        when(submissionService.findAllByStudentIdAndCourseId(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        assertThrows(SystemException.class, () -> courseServiceImpl.getMyCourses(1L));
        verify(userRepository).findById(Mockito.<Long>any());
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
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#addInstructorToCourse(Long, Long)}
     */
    @Test
    void testAddInstructorToCourse() {
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        Optional<Instructor> ofResult2 = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(courseResponseDTO);
        assertSame(courseResponseDTO, courseServiceImpl.addInstructorToCourse(1L, 1L));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(instructorRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toResponseDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#addInstructorToCourse(Long, Long)}
     */
    @Test
    void testAddInstructorToCourse2() {
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        Optional<Instructor> ofResult2 = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> courseServiceImpl.addInstructorToCourse(1L, 1L));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(instructorRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toResponseDto(Mockito.<Course>any());
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
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        Optional<Instructor> ofResult2 = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(courseResponseDTO);
        assertSame(courseResponseDTO, courseServiceImpl.addInstructorToCourse(1L, 1L));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).addInstructor(Mockito.<Instructor>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(instructorRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toResponseDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#addInstructorToCourse(Long, Long)}
     */
    @Test
    void testAddInstructorToCourse4() {
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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        Course course2 = mock(Course.class);
        doNothing().when(course2).setCreatedBy(Mockito.<String>any());
        doNothing().when(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).setModifiedBy(Mockito.<String>any());
        doNothing().when(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).addInstructor(Mockito.<Instructor>any());
        doNothing().when(course2).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course2).setDescription(Mockito.<String>any());
        doNothing().when(course2).setId(Mockito.<Long>any());
        doNothing().when(course2).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course2).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course2).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course2).setTitle(Mockito.<String>any());
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
        Optional<Instructor> ofResult = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(new CourseResponseDTO());
        assertThrows(SystemException.class, () -> courseServiceImpl.addInstructorToCourse(1L, 1L));
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course2).setCreatedBy(Mockito.<String>any());
        verify(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course2).setModifiedBy(Mockito.<String>any());
        verify(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course2).setAvailable(Mockito.<Boolean>any());
        verify(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course2).setDescription(Mockito.<String>any());
        verify(course2).setId(Mockito.<Long>any());
        verify(course2).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course2).setLessons(Mockito.<Set<Lesson>>any());
        verify(course2).setStudents(Mockito.<Set<Student>>any());
        verify(course2).setTitle(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#deleteInstructorForCourse(Long, Long)}
     */
    @Test
    void testDeleteInstructorForCourse() {
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        Optional<Instructor> ofResult2 = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(courseResponseDTO);
        assertSame(courseResponseDTO, courseServiceImpl.deleteInstructorForCourse(1L, 1L));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(instructorRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toResponseDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#deleteInstructorForCourse(Long, Long)}
     */
    @Test
    void testDeleteInstructorForCourse2() {
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        Optional<Instructor> ofResult2 = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> courseServiceImpl.deleteInstructorForCourse(1L, 1L));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(instructorRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toResponseDto(Mockito.<Course>any());
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
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        Optional<Instructor> ofResult2 = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(courseResponseDTO);
        assertSame(courseResponseDTO, courseServiceImpl.deleteInstructorForCourse(1L, 1L));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).removeInstructor(Mockito.<Instructor>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(instructorRepository).findById(Mockito.<Long>any());
        verify(courseMapper).toResponseDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#deleteInstructorForCourse(Long, Long)}
     */
    @Test
    void testDeleteInstructorForCourse4() {
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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        Course course2 = mock(Course.class);
        doNothing().when(course2).setCreatedBy(Mockito.<String>any());
        doNothing().when(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).setModifiedBy(Mockito.<String>any());
        doNothing().when(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course2).removeInstructor(Mockito.<Instructor>any());
        doNothing().when(course2).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course2).setDescription(Mockito.<String>any());
        doNothing().when(course2).setId(Mockito.<Long>any());
        doNothing().when(course2).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course2).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course2).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course2).setTitle(Mockito.<String>any());
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
        Optional<Instructor> ofResult = Optional.of(instructor);
        when(instructorRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(new CourseResponseDTO());
        assertThrows(SystemException.class, () -> courseServiceImpl.deleteInstructorForCourse(1L, 1L));
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course2).setCreatedBy(Mockito.<String>any());
        verify(course2).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course2).setModifiedBy(Mockito.<String>any());
        verify(course2).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course2).setAvailable(Mockito.<Boolean>any());
        verify(course2).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course2).setDescription(Mockito.<String>any());
        verify(course2).setId(Mockito.<Long>any());
        verify(course2).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course2).setLessons(Mockito.<Set<Lesson>>any());
        verify(course2).setStudents(Mockito.<Set<Student>>any());
        verify(course2).setTitle(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#updateCourseLessonsAndSave(Long, LessonsUpdateDTO)}
     */
    @Test
    void testUpdateCourseLessons() {

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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        courseServiceImpl.updateCourseLessonsAndSave(1L, new LessonsUpdateDTO(new HashSet<>()));
    }

    /**
     * Method under test: {@link CourseServiceImpl#updateCourseLessonsAndSave(Long, LessonsUpdateDTO)}
     */
    @Test
    void testUpdateCourseLessons2() {

        Course course = mock(Course.class);
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        courseServiceImpl.updateCourseLessonsAndSave(1L, new LessonsUpdateDTO(new HashSet<>()));
    }

    /**
     * Method under test: {@link CourseServiceImpl#updateCourseLessonsAndSave(Long, LessonsUpdateDTO)}
     */
    @Test
    void testUpdateCourseLessons3() {
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        Course course = mock(Course.class);
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        assertThrows(SystemException.class, () -> courseServiceImpl.updateCourseLessonsAndSave(1L, new LessonsUpdateDTO()));
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#updateCourseLessonsAndSave(Long, LessonsUpdateDTO)}
     */
    @Test
    void testUpdateCourseLessons4() {
        Course course = mock(Course.class);
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(courseResponseDTO);

        LessonsUpdateDTO lessonsDTO = new LessonsUpdateDTO();
        lessonsDTO.setLessons(new HashSet<>());
        assertSame(courseResponseDTO, courseServiceImpl.updateCourseLessonsAndSave(1L, lessonsDTO));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).getLessons();
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(courseMapper).toResponseDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#updateCourseLessonsAndSave(Long, LessonsUpdateDTO)}
     */
    @Test
    void testUpdateCourseLessons5() {
        Course course = mock(Course.class);
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenThrow(new SystemException(ErrorCode.OK));

        LessonsUpdateDTO lessonsDTO = new LessonsUpdateDTO();
        lessonsDTO.setLessons(new HashSet<>());
        assertThrows(SystemException.class, () -> courseServiceImpl.updateCourseLessonsAndSave(1L, lessonsDTO));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).getLessons();
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(courseMapper).toResponseDto(Mockito.<Course>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#updateCourseLessonsAndSave(Long, LessonsUpdateDTO)}
     */
    @Test
    void testUpdateCourseLessons6() {
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1L);
        doNothing().when(course).addLesson(Mockito.<Lesson>any());
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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

        Lesson lesson = new Lesson();
        lesson.setCourse(course3);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");
        when(lessonRepository.save(Mockito.<Lesson>any())).thenReturn(lesson);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(courseResponseDTO);

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

        Lesson lesson2 = new Lesson();
        lesson2.setCourse(course4);
        lesson2.setCreatedBy("Anonymous");
        lesson2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setDescription("The characteristics of someone or something");
        lesson2.setId(1L);
        lesson2.setModifiedBy("Anonymous");
        lesson2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson2.setSubmissions(new HashSet<>());
        lesson2.setTitle("Dr");
        when(lessonMapper.fromRequestDto(Mockito.<LessonRequestDTO>any())).thenReturn(lesson2);

        HashSet<LessonRequestDTO> lessons = new HashSet<>();
        lessons.add(new LessonRequestDTO());

        LessonsUpdateDTO lessonsDTO = new LessonsUpdateDTO();
        lessonsDTO.setLessons(lessons);
        assertSame(courseResponseDTO, courseServiceImpl.updateCourseLessonsAndSave(1L, lessonsDTO));
        verify(courseRepository).save(Mockito.<Course>any());
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).getId();
        verify(course).getLessons();
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).addLesson(Mockito.<Lesson>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(lessonRepository).save(Mockito.<Lesson>any());
        verify(courseMapper).toResponseDto(Mockito.<Course>any());
        verify(lessonMapper).fromRequestDto(Mockito.<LessonRequestDTO>any());
    }

    /**
     * Method under test: {@link CourseServiceImpl#updateCourseLessonsAndSave(Long, LessonsUpdateDTO)}
     */
    @Test
    void testUpdateCourseLessons7() {
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1L);
        doNothing().when(course).addLesson(Mockito.<Lesson>any());
        when(course.getLessons()).thenReturn(new HashSet<>());
        doNothing().when(course).setCreatedBy(Mockito.<String>any());
        doNothing().when(course).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setModifiedBy(Mockito.<String>any());
        doNothing().when(course).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(course).setAvailable(Mockito.<Boolean>any());
        doNothing().when(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        doNothing().when(course).setDescription(Mockito.<String>any());
        doNothing().when(course).setId(Mockito.<Long>any());
        doNothing().when(course).setInstructors(Mockito.<Set<Instructor>>any());
        doNothing().when(course).setLessons(Mockito.<Set<Lesson>>any());
        doNothing().when(course).setStudents(Mockito.<Set<Student>>any());
        doNothing().when(course).setTitle(Mockito.<String>any());
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
        Optional<Course> ofResult = Optional.of(course);

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
        when(courseRepository.save(Mockito.<Course>any())).thenReturn(course2);
        when(courseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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

        Lesson lesson = new Lesson();
        lesson.setCourse(course3);
        lesson.setCreatedBy("Anonymous");
        lesson.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setDescription("The characteristics of someone or something");
        lesson.setId(1L);
        lesson.setModifiedBy("Anonymous");
        lesson.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lesson.setSubmissions(new HashSet<>());
        lesson.setTitle("Dr");
        when(lessonRepository.save(Mockito.<Lesson>any())).thenReturn(lesson);
        when(courseMapper.toResponseDto(Mockito.<Course>any())).thenReturn(new CourseResponseDTO());
        when(lessonMapper.fromRequestDto(Mockito.<LessonRequestDTO>any())).thenThrow(new SystemException(ErrorCode.OK));

        HashSet<LessonRequestDTO> lessons = new HashSet<>();
        lessons.add(new LessonRequestDTO());

        LessonsUpdateDTO lessonsDTO = new LessonsUpdateDTO();
        lessonsDTO.setLessons(lessons);
        assertThrows(SystemException.class, () -> courseServiceImpl.updateCourseLessonsAndSave(1L, lessonsDTO));
        verify(courseRepository).findById(Mockito.<Long>any());
        verify(course).getId();
        verify(course).getLessons();
        verify(course).setCreatedBy(Mockito.<String>any());
        verify(course).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(course).setModifiedBy(Mockito.<String>any());
        verify(course).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(course).setAvailable(Mockito.<Boolean>any());
        verify(course).setCourseFeedbacks(Mockito.<Set<CourseFeedback>>any());
        verify(course).setDescription(Mockito.<String>any());
        verify(course).setId(Mockito.<Long>any());
        verify(course).setInstructors(Mockito.<Set<Instructor>>any());
        verify(course).setLessons(Mockito.<Set<Lesson>>any());
        verify(course).setStudents(Mockito.<Set<Student>>any());
        verify(course).setTitle(Mockito.<String>any());
        verify(lessonMapper).fromRequestDto(Mockito.<LessonRequestDTO>any());
    }
}

