package com.example.courseapi.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.courseapi.config.args.generic.FilterImpl;
import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.generic.FiltersImpl;
import com.example.courseapi.config.args.generic.SpecificationComparison;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.request.CourseFeedbackRequestDTO;
import com.example.courseapi.dto.response.CourseFeedbackResponseDTO;
import com.example.courseapi.repository.CourseFeedbackRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.service.mapper.CourseFeedbackMapper;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CourseFeedbackServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CourseFeedbackServiceImplTest {
    @MockBean
    private CourseFeedbackMapper courseFeedbackMapper;

    @MockBean
    private CourseFeedbackRepository courseFeedbackRepository;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private CourseFeedbackServiceImpl courseFeedbackServiceImpl;

    /**
     * Method under test: {@link CourseFeedbackServiceImpl#findById(Long)}
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

        CourseFeedback courseFeedback = new CourseFeedback();
        courseFeedback.setCourse(course);
        courseFeedback.setCreatedBy("Anonymous");
        courseFeedback.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        courseFeedback.setFeedback("Feedback");
        courseFeedback.setId(1L);
        courseFeedback.setModifiedBy("Anonymous");
        courseFeedback.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        courseFeedback.setStudent(student);
        Optional<CourseFeedback> ofResult = Optional.of(courseFeedback);
        when(courseFeedbackRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(courseFeedbackMapper.toResponseDto(Mockito.<CourseFeedback>any()))
                .thenReturn(new CourseFeedbackResponseDTO());
        assertTrue(courseFeedbackServiceImpl.findById(1L).isPresent());
        verify(courseFeedbackRepository).findById(Mockito.<Long>any());
        verify(courseFeedbackMapper).toResponseDto(Mockito.<CourseFeedback>any());
    }

    /**
     * Method under test: {@link CourseFeedbackServiceImpl#save(CourseFeedbackRequestDTO, Student)}
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

        CourseFeedback courseFeedback = new CourseFeedback();
        courseFeedback.setCourse(course);
        courseFeedback.setCreatedBy("Anonymous");
        courseFeedback.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        courseFeedback.setFeedback("Feedback");
        courseFeedback.setId(1L);
        courseFeedback.setModifiedBy("Anonymous");
        courseFeedback.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        courseFeedback.setStudent(student);
        when(courseFeedbackRepository.save(Mockito.<CourseFeedback>any())).thenReturn(courseFeedback);

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

        CourseFeedback courseFeedback2 = new CourseFeedback();
        courseFeedback2.setCourse(course2);
        courseFeedback2.setCreatedBy("Anonymous");
        courseFeedback2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        courseFeedback2.setFeedback("Feedback");
        courseFeedback2.setId(1L);
        courseFeedback2.setModifiedBy("Anonymous");
        courseFeedback2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        courseFeedback2.setStudent(student2);
        CourseFeedbackResponseDTO courseFeedbackResponseDTO = new CourseFeedbackResponseDTO();
        when(courseFeedbackMapper.toResponseDto(Mockito.<CourseFeedback>any())).thenReturn(courseFeedbackResponseDTO);
        when(courseFeedbackMapper.fromRequestDto(Mockito.<CourseFeedbackRequestDTO>any())).thenReturn(courseFeedback2);
        CourseFeedbackRequestDTO courseDTO = new CourseFeedbackRequestDTO();

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
        when(studentRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(student3));
        assertSame(courseFeedbackResponseDTO, courseFeedbackServiceImpl.save(courseDTO, student3.getId()));
        verify(courseFeedbackRepository).save(Mockito.<CourseFeedback>any());
        verify(courseFeedbackMapper).fromRequestDto(Mockito.<CourseFeedbackRequestDTO>any());
        verify(courseFeedbackMapper).toResponseDto(Mockito.<CourseFeedback>any());
    }

    /**
     * Method under test: {@link CourseFeedbackServiceImpl#findAll(Filters, Pageable)}
     */
    @Test
    void testFindAll() {
        when(courseFeedbackRepository.findAll(Mockito.<Specification<CourseFeedback>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(courseFeedbackServiceImpl.findAll(new FiltersImpl(), null).toList().isEmpty());
        verify(courseFeedbackRepository).findAll(Mockito.<Specification<CourseFeedback>>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link CourseFeedbackServiceImpl#findAll(Filters, Pageable)}
     */
    @Test
    void testFindAll2() {
        when(courseFeedbackRepository.findAll(Mockito.<Specification<CourseFeedback>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        courseFeedbackServiceImpl.findAll(new FiltersImpl(), PageRequest.of(0, 5));
    }

    /**
     * Method under test: {@link CourseFeedbackServiceImpl#findAll(Filters, Pageable)}
     */
    @Test
    void testFindAll3() {
        when(courseFeedbackRepository.findAll(Mockito.<Specification<CourseFeedback>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        FiltersImpl filters = new FiltersImpl();
        filters.add(new FilterImpl("Name", SpecificationComparison.ENDS_WITH, "Value"));
        assertTrue(courseFeedbackServiceImpl.findAll(filters, null).toList().isEmpty());
        verify(courseFeedbackRepository).findAll(Mockito.<Specification<CourseFeedback>>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link CourseFeedbackServiceImpl#findAll(Filters, Pageable)}
     */
    @Test
    void testFindAll4() {
        when(courseFeedbackRepository.findAll(Mockito.<Specification<CourseFeedback>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        FiltersImpl filters = new FiltersImpl();
        filters.add(new FilterImpl("Name", SpecificationComparison.ENDS_WITH, "Value"));
        filters.add(new FilterImpl("Name", SpecificationComparison.ENDS_WITH, "Value"));
        assertTrue(courseFeedbackServiceImpl.findAll(filters, null).toList().isEmpty());
        verify(courseFeedbackRepository).findAll(Mockito.<Specification<CourseFeedback>>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link CourseFeedbackServiceImpl#findByStudentId(Long)}
     */
    @Test
    void testFindByStudentId() {
        when(courseFeedbackRepository.findByStudentId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<CourseFeedbackResponseDTO> courseFeedbackResponseDTOList = new ArrayList<>();
        when(courseFeedbackMapper.toResponseDto(Mockito.<List<CourseFeedback>>any()))
                .thenReturn(courseFeedbackResponseDTOList);
        List<CourseFeedbackResponseDTO> actualFindByStudentIdResult = courseFeedbackServiceImpl.findByStudentId(1L);
        assertSame(courseFeedbackResponseDTOList, actualFindByStudentIdResult);
        assertTrue(actualFindByStudentIdResult.isEmpty());
        verify(courseFeedbackRepository).findByStudentId(Mockito.<Long>any());
        verify(courseFeedbackMapper).toResponseDto(Mockito.<List<CourseFeedback>>any());
    }

    /**
     * Method under test: {@link CourseFeedbackServiceImpl#findByCourseId(Long)}
     */
    @Test
    void testFindByCourseId() {
        when(courseFeedbackRepository.findByCourseId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<CourseFeedbackResponseDTO> courseFeedbackResponseDTOList = new ArrayList<>();
        when(courseFeedbackMapper.toResponseDto(Mockito.<List<CourseFeedback>>any()))
                .thenReturn(courseFeedbackResponseDTOList);
        List<CourseFeedbackResponseDTO> actualFindByCourseIdResult = courseFeedbackServiceImpl.findByCourseId(1L);
        assertSame(courseFeedbackResponseDTOList, actualFindByCourseIdResult);
        assertTrue(actualFindByCourseIdResult.isEmpty());
        verify(courseFeedbackRepository).findByCourseId(Mockito.<Long>any());
        verify(courseFeedbackMapper).toResponseDto(Mockito.<List<CourseFeedback>>any());
    }

    /**
     * Method under test: {@link CourseFeedbackServiceImpl#delete(Long)}
     */
    @Test
    void testDelete() {
        doNothing().when(courseFeedbackRepository).deleteById(Mockito.<Long>any());
        courseFeedbackServiceImpl.delete(1L);
        verify(courseFeedbackRepository).deleteById(Mockito.<Long>any());
    }
}

