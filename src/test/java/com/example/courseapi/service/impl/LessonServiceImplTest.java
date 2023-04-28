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
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.LessonDTO;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.service.mapper.LessonMapper;

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

@ContextConfiguration(classes = {LessonServiceImpl.class})
@ExtendWith(SpringExtension.class)
class LessonServiceImplTest {
    @MockBean
    private LessonMapper lessonMapper;

    @MockBean
    private LessonRepository lessonRepository;

    @Autowired
    private LessonServiceImpl lessonServiceImpl;

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
     * Method under test: {@link LessonServiceImpl#findById(Long)}
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
        Optional<Lesson> ofResult = Optional.of(lesson);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setCourseId(1L);
        lessonDTO.setCreatedBy("username");
        lessonDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lessonDTO.setDescription("The characteristics of someone or something");
        lessonDTO.setId(1L);
        lessonDTO.setModifiedBy("username");
        lessonDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lessonDTO.setTitle("Dr");
        when(lessonMapper.toDto(Mockito.<Lesson>any())).thenReturn(lessonDTO);
        assertTrue(lessonServiceImpl.findById(1L).isPresent());
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(lessonMapper).toDto(Mockito.<Lesson>any());
    }

    /**
     * Method under test: {@link LessonServiceImpl#save(LessonDTO)}
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
        when(lessonRepository.save(Mockito.<Lesson>any())).thenReturn(lesson);

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

        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setCourseId(1L);
        lessonDTO.setCreatedBy("username");
        lessonDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lessonDTO.setDescription("The characteristics of someone or something");
        lessonDTO.setId(1L);
        lessonDTO.setModifiedBy("username");
        lessonDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lessonDTO.setTitle("Dr");
        when(lessonMapper.toEntity(Mockito.<LessonDTO>any())).thenReturn(lesson2);
        when(lessonMapper.toDto(Mockito.<Lesson>any())).thenReturn(lessonDTO);

        LessonDTO lessonDTO2 = new LessonDTO();
        lessonDTO2.setCourseId(1L);
        lessonDTO2.setCreatedBy("username");
        lessonDTO2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lessonDTO2.setDescription("The characteristics of someone or something");
        lessonDTO2.setId(1L);
        lessonDTO2.setModifiedBy("username");
        lessonDTO2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        lessonDTO2.setTitle("Dr");
        assertSame(lessonDTO, lessonServiceImpl.save(lessonDTO2));
        verify(lessonRepository).save(Mockito.<Lesson>any());
        verify(lessonMapper).toEntity(Mockito.<LessonDTO>any());
        verify(lessonMapper).toDto(Mockito.<Lesson>any());
    }

    /**
     * Method under test: {@link LessonServiceImpl#findAll(Filters, Pageable)}
     */
    @Test
    void testFindAll() {
        when(lessonRepository.findAll(Mockito.<Specification<Lesson>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(lessonServiceImpl.findAll(new FiltersImpl(), null).toList().isEmpty());
        verify(lessonRepository).findAll(Mockito.<Specification<Lesson>>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link LessonServiceImpl#findAll(Filters, Pageable)}
     */
    @Test
    void testFindAll3() {
        when(lessonRepository.findAll(Mockito.<Specification<Lesson>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        FiltersImpl filters = new FiltersImpl();
        filters.add(new FilterImpl("Name", SpecificationComparison.ENDS_WITH, "Value"));
        assertTrue(lessonServiceImpl.findAll(filters, null).toList().isEmpty());
        verify(lessonRepository).findAll(Mockito.<Specification<Lesson>>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link LessonServiceImpl#findAll(Filters, Pageable)}
     */
    @Test
    void testFindAll4() {
        when(lessonRepository.findAll(Mockito.<Specification<Lesson>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        FiltersImpl filters = new FiltersImpl();
        filters.add(new FilterImpl("Name", SpecificationComparison.ENDS_WITH, "Value"));
        filters.add(new FilterImpl("Name", SpecificationComparison.ENDS_WITH, "Value"));
        assertTrue(lessonServiceImpl.findAll(filters, null).toList().isEmpty());
        verify(lessonRepository).findAll(Mockito.<Specification<Lesson>>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link LessonServiceImpl#findByCourseId(Long)}
     */
    @Test
    void testFindByCourseId() {
        when(lessonRepository.findByCourseId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<LessonDTO> lessonDTOList = new ArrayList<>();
        when(lessonMapper.toDto(Mockito.<List<Lesson>>any())).thenReturn(lessonDTOList);
        List<LessonDTO> actualFindByCourseIdResult = lessonServiceImpl.findByCourseId(1L);
        assertSame(lessonDTOList, actualFindByCourseIdResult);
        assertTrue(actualFindByCourseIdResult.isEmpty());
        verify(lessonRepository).findByCourseId(Mockito.<Long>any());
        verify(lessonMapper).toDto(Mockito.<List<Lesson>>any());
    }

    /**
     * Method under test: {@link LessonServiceImpl#delete(Long)}
     */
    @Test
    void testDelete() {
        doNothing().when(lessonRepository).deleteById(Mockito.<Long>any());
        lessonServiceImpl.delete(1L);
        verify(lessonRepository).deleteById(Mockito.<Long>any());
    }
}

