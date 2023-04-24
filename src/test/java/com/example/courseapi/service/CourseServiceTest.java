package com.example.courseapi.service;

import com.example.courseapi.domain.Course;
import com.example.courseapi.dto.CourseDTO;
import com.example.courseapi.repository.CourseRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.impl.CourseServiceImpl;
import com.example.courseapi.service.mapper.CourseMapper;
import com.example.courseapi.service.mapper.CourseMapperImpl;
import com.example.courseapi.service.mapper.CourseStatusMapper;
import com.example.courseapi.service.mapper.CourseStatusMapperImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    private CourseMapper courseMapper;
    private CourseStatusMapper courseStatusMapper;

    @Mock
    private CourseRepository courseRepositoryMock;

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private SubmissionService submissionService;

    private CourseService courseService;

    private Course course;
    private CourseDTO courseDTO;
    private List<Course> courses;
    private List<CourseDTO> courseDTOs;
    private Long courseId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up courseService
        courseMapper = new CourseMapperImpl();
        courseStatusMapper = new CourseStatusMapperImpl();
        courseService = new CourseServiceImpl(courseRepositoryMock, studentRepository, userRepository, courseMapper, courseStatusMapper, submissionService);

        // Set the course id
        courseId = 1L;

        // Create a sample course
        course = Course.builder()
                .id(courseId)
                .title("Test Course")
                .description("This is a test course")
                .build();
        courseDTO = courseMapper.toDto(course);

        // Create a list of courses
        courses = Collections.singletonList(course);
        courseDTOs = Collections.singletonList(courseDTO);
    }

    @Test
    public void testSaveCourse() {
        // Configure the mock repository to return the saved course when the save method is called
        when(courseRepositoryMock.save(course)).thenReturn(course);

        // Call the service method to save the course
        CourseDTO savedCourse = courseService.save(courseMapper.toDto(course));

        // Verify that the save method was called on the mock repository
        verify(courseRepositoryMock, Mockito.times(1)).save(course);

        // Assert that the saved course is equal to the original course
        assertEquals(courseDTO, savedCourse);
    }

    @Test
    public void testFindAllCourses() {
        // Configure the mock repository to return the list of courses when the findAll method is called
        when(courseRepositoryMock.findAll()).thenReturn(courses);

        // Call the service method to find all the courses
        List<CourseDTO> actualCourses = courseService.findAll();

        // Verify that the findAll method was called on the mock repository
        verify(courseRepositoryMock, Mockito.times(1)).findAll();

        // Assert that the actual courses are equal to the expected courses
        assertEquals(courseDTOs, actualCourses);
    }

    @Test
    public void testFindCourseById() {
        // Configure the mock repository to return the optional containing the course when the findById method is called
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(course));

        // Call the service method to find the course by id
        Optional<CourseDTO> actualCourse = courseService.findById(courseId);

        // Verify that the findById method was called on the mock repository
        verify(courseRepositoryMock, Mockito.times(1)).findById(courseId);

        // Assert that the actual course is equal to the expected course
        assertTrue(actualCourse.isPresent());
        assertEquals(courseDTO, actualCourse.get());
    }

    @Test
    public void testFindCourseByIdNotFound() {
        // Configure the mock repository to return an empty optional when the findById method is called
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.empty());

        // Call the service method to find the course by id
        Optional<CourseDTO> actualCourse = courseService.findById(courseId);

        // Verify that the findById method was called on the mock repository
        verify(courseRepositoryMock, Mockito.times(1)).findById(courseId);

        // Assert that the actual course is not present
        assertFalse(actualCourse.isPresent());
    }

    @Test
    public void testDeleteCourseById() {
        // Call the service method to delete the course by id
        courseService.delete(courseId);

        // Verify that the deleteById method was called on the mock repository with the correct course id
        verify(courseRepositoryMock, Mockito.times(1)).deleteById(courseId);
    }

    @Test
    public void testFindByStudentsId() {
        // Configure the mock repository to return the list of courses when the findByStudentsId method is called
        when(courseRepositoryMock.findByStudentsId(courseId)).thenReturn(courses);

        // Call the service method to find the courses by student id
        List<CourseDTO> actualCourses = courseService.findByStudentId(courseId);

        // Verify that the findByStudentsId method was called on the mock repository
        verify(courseRepositoryMock, Mockito.times(1)).findByStudentsId(courseId);

        // Assert that the actual courses are equal to the expected courses
        assertEquals(courseDTOs, actualCourses);
    }
}