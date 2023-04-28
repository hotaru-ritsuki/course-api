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
import com.example.courseapi.dto.CourseFeedbackDTO;
import com.example.courseapi.repository.CourseFeedbackRepository;
import com.example.courseapi.service.mapper.CourseFeedbackMapper;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

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

@ContextConfiguration(classes = {CourseFeedbackServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CourseFeedbackServiceImplTest {
    @MockBean
    private CourseFeedbackMapper courseFeedbackMapper;

    @MockBean
    private CourseFeedbackRepository courseFeedbackRepository;

    @Autowired
    private CourseFeedbackServiceImpl courseFeedbackServiceImpl;

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
     * Method under test: {@link CourseFeedbackServiceImpl#findById(Long)}
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

        Student student = new Student();
//        //student.setCourseFeedbacks(new HashSet<>());
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

        CourseFeedback courseFeedback = new CourseFeedback();
        courseFeedback.setCourse(course);
        courseFeedback.setCreatedBy("username");
        courseFeedback.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseFeedback.setFeedback("Feedback");
        courseFeedback.setId(1L);
        courseFeedback.setModifiedBy("username");
        courseFeedback.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseFeedback.setStudent(student);
        Optional<CourseFeedback> ofResult = Optional.of(courseFeedback);
        when(courseFeedbackRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        CourseFeedbackDTO courseFeedbackDTO = new CourseFeedbackDTO();
        courseFeedbackDTO.setCourseId(1L);
        courseFeedbackDTO.setCreatedBy("username");
        courseFeedbackDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseFeedbackDTO.setFeedback("Feedback");
        courseFeedbackDTO.setId(1L);
        courseFeedbackDTO.setModifiedBy("username");
        courseFeedbackDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseFeedbackDTO.setStudentId(1L);
        when(courseFeedbackMapper.toDto(Mockito.<CourseFeedback>any())).thenReturn(courseFeedbackDTO);
        assertTrue(courseFeedbackServiceImpl.findById(1L).isPresent());
        verify(courseFeedbackRepository).findById(Mockito.<Long>any());
        verify(courseFeedbackMapper).toDto(Mockito.<CourseFeedback>any());
    }

    /**
     * Method under test: {@link CourseFeedbackServiceImpl#save(CourseFeedbackDTO)}
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

        Student student = new Student();
//        //student.setCourseFeedbacks(new HashSet<>());
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

        CourseFeedback courseFeedback = new CourseFeedback();
        courseFeedback.setCourse(course);
        courseFeedback.setCreatedBy("username");
        courseFeedback.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseFeedback.setFeedback("Feedback");
        courseFeedback.setId(1L);
        courseFeedback.setModifiedBy("username");
        courseFeedback.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseFeedback.setStudent(student);
        when(courseFeedbackRepository.save(Mockito.<CourseFeedback>any())).thenReturn(courseFeedback);

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

        CourseFeedback courseFeedback2 = new CourseFeedback();
        courseFeedback2.setCourse(course2);
        courseFeedback2.setCreatedBy("username");
        courseFeedback2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseFeedback2.setFeedback("Feedback");
        courseFeedback2.setId(1L);
        courseFeedback2.setModifiedBy("username");
        courseFeedback2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseFeedback2.setStudent(student2);

        CourseFeedbackDTO courseFeedbackDTO = new CourseFeedbackDTO();
        courseFeedbackDTO.setCourseId(1L);
        courseFeedbackDTO.setCreatedBy("username");
        courseFeedbackDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseFeedbackDTO.setFeedback("Feedback");
        courseFeedbackDTO.setId(1L);
        courseFeedbackDTO.setModifiedBy("username");
        courseFeedbackDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseFeedbackDTO.setStudentId(1L);
        when(courseFeedbackMapper.toEntity(Mockito.<CourseFeedbackDTO>any())).thenReturn(courseFeedback2);
        when(courseFeedbackMapper.toDto(Mockito.<CourseFeedback>any())).thenReturn(courseFeedbackDTO);

        CourseFeedbackDTO courseDTO = new CourseFeedbackDTO();
        courseDTO.setCourseId(1L);
        courseDTO.setCreatedBy("username");
        courseDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setFeedback("Feedback");
        courseDTO.setId(1L);
        courseDTO.setModifiedBy("username");
        courseDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        courseDTO.setStudentId(1L);
        assertSame(courseFeedbackDTO, courseFeedbackServiceImpl.save(courseDTO));
        verify(courseFeedbackRepository).save(Mockito.<CourseFeedback>any());
        verify(courseFeedbackMapper).toEntity(Mockito.<CourseFeedbackDTO>any());
        verify(courseFeedbackMapper).toDto(Mockito.<CourseFeedback>any());
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
        ArrayList<CourseFeedbackDTO> courseFeedbackDTOList = new ArrayList<>();
        when(courseFeedbackMapper.toDto(Mockito.<List<CourseFeedback>>any())).thenReturn(courseFeedbackDTOList);
        List<CourseFeedbackDTO> actualFindByStudentIdResult = courseFeedbackServiceImpl.findByStudentId(1L);
        assertSame(courseFeedbackDTOList, actualFindByStudentIdResult);
        assertTrue(actualFindByStudentIdResult.isEmpty());
        verify(courseFeedbackRepository).findByStudentId(Mockito.<Long>any());
        verify(courseFeedbackMapper).toDto(Mockito.<List<CourseFeedback>>any());
    }

    /**
     * Method under test: {@link CourseFeedbackServiceImpl#findByCourseId(Long)}
     */
    @Test
    void testFindByCourseId() {
        when(courseFeedbackRepository.findByCourseId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<CourseFeedbackDTO> courseFeedbackDTOList = new ArrayList<>();
        when(courseFeedbackMapper.toDto(Mockito.<List<CourseFeedback>>any())).thenReturn(courseFeedbackDTOList);
        List<CourseFeedbackDTO> actualFindByCourseIdResult = courseFeedbackServiceImpl.findByCourseId(1L);
        assertSame(courseFeedbackDTOList, actualFindByCourseIdResult);
        assertTrue(actualFindByCourseIdResult.isEmpty());
        verify(courseFeedbackRepository).findByCourseId(Mockito.<Long>any());
        verify(courseFeedbackMapper).toDto(Mockito.<List<CourseFeedback>>any());
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

