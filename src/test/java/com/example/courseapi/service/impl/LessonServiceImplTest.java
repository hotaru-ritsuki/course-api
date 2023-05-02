package com.example.courseapi.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.courseapi.config.args.generic.FilterImpl;
import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.generic.FiltersImpl;
import com.example.courseapi.config.args.generic.SpecificationComparison;
import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.request.LessonRequestDTO;
import com.example.courseapi.dto.response.LessonResponseDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.service.mapper.LessonMapper;

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

@ContextConfiguration(classes = {LessonServiceImpl.class})
@ExtendWith(SpringExtension.class)
class LessonServiceImplTest {
    @MockBean
    private LessonMapper lessonMapper;

    @MockBean
    private LessonRepository lessonRepository;

    @Autowired
    private LessonServiceImpl lessonServiceImpl;

    /**
     * Method under test: {@link LessonServiceImpl#findById(Long)}
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
        Optional<Lesson> ofResult = Optional.of(lesson);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(lessonMapper.toResponseDto(Mockito.<Lesson>any())).thenReturn(new LessonResponseDTO());
        assertTrue(lessonServiceImpl.findById(1L).isPresent());
        verify(lessonRepository).findById(Mockito.<Long>any());
        verify(lessonMapper).toResponseDto(Mockito.<Lesson>any());
    }

    /**
     * Method under test: {@link LessonServiceImpl#findById(Long)}
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
        Optional<Lesson> ofResult = Optional.of(lesson);
        when(lessonRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(lessonMapper.toResponseDto(Mockito.<Lesson>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> lessonServiceImpl.findById(1L));
    }

    /**
     * Method under test: {@link LessonServiceImpl#save(LessonRequestDTO)}
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
        when(lessonRepository.save(Mockito.<Lesson>any())).thenReturn(lesson);

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
        when(lessonMapper.fromRequestDto(Mockito.<LessonRequestDTO>any())).thenReturn(lesson2);
        LessonResponseDTO lessonResponseDTO = new LessonResponseDTO();
        when(lessonMapper.toResponseDto(Mockito.<Lesson>any())).thenReturn(lessonResponseDTO);
        assertSame(lessonResponseDTO, lessonServiceImpl.save(new LessonRequestDTO()));
        verify(lessonRepository).save(Mockito.<Lesson>any());
        verify(lessonMapper).fromRequestDto(Mockito.<LessonRequestDTO>any());
        verify(lessonMapper).toResponseDto(Mockito.<Lesson>any());
    }

    /**
     * Method under test: {@link LessonServiceImpl#save(LessonRequestDTO)}
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
        when(lessonRepository.save(Mockito.<Lesson>any())).thenReturn(lesson);
        when(lessonMapper.fromRequestDto(Mockito.<LessonRequestDTO>any())).thenThrow(new SystemException(ErrorCode.OK));
        when(lessonMapper.toResponseDto(Mockito.<Lesson>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> lessonServiceImpl.save(new LessonRequestDTO()));
        verify(lessonMapper).fromRequestDto(Mockito.<LessonRequestDTO>any());
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

        ArrayList<Lesson> content = new ArrayList<>();
        content.add(lesson);
        PageImpl<Lesson> pageImpl = new PageImpl<>(content);
        when(lessonRepository.findAll(Mockito.<Specification<Lesson>>any(), Mockito.<Pageable>any()))
                .thenReturn(pageImpl);
        lessonServiceImpl.findAll(new FiltersImpl(), PageRequest.of(0, 8));
    }

    /**
     * Method under test: {@link LessonServiceImpl#findAll(Filters, Pageable)}
     */
    @Test
    void testFindAll3() {

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

        ArrayList<Lesson> content = new ArrayList<>();
        content.add(lesson);
        PageImpl<Lesson> pageImpl = new PageImpl<>(content);
        when(lessonRepository.findAll(Mockito.<Specification<Lesson>>any(), Mockito.<Pageable>any()))
                .thenReturn(pageImpl);

        FiltersImpl filters = new FiltersImpl();
        filters.add(new FilterImpl("Name", SpecificationComparison.ENDS_WITH, "Value"));
        lessonServiceImpl.findAll(filters, PageRequest.of(0, 8));
    }

    /**
     * Method under test: {@link LessonServiceImpl#findAll(Filters, Pageable)}
     */
    @Test
    void testFindAll4() {

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

        ArrayList<Lesson> content = new ArrayList<>();
        content.add(lesson);
        PageImpl<Lesson> pageImpl = new PageImpl<>(content);
        when(lessonRepository.findAll(Mockito.<Specification<Lesson>>any(), Mockito.<Pageable>any()))
                .thenReturn(pageImpl);

        FiltersImpl filters = new FiltersImpl();
        filters.add(new FilterImpl("Name", SpecificationComparison.ENDS_WITH, "Value"));
        filters.add(new FilterImpl("Name", SpecificationComparison.ENDS_WITH, "Value"));
        lessonServiceImpl.findAll(filters, PageRequest.of(0, 8));
    }

    /**
     * Method under test: {@link LessonServiceImpl#findByCourseId(Long)}
     */
    @Test
    void testFindByCourseId() {
        when(lessonRepository.findByCourseId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        ArrayList<LessonResponseDTO> lessonResponseDTOList = new ArrayList<>();
        when(lessonMapper.toResponseDto(Mockito.<List<Lesson>>any())).thenReturn(lessonResponseDTOList);
        List<LessonResponseDTO> actualFindByCourseIdResult = lessonServiceImpl.findByCourseId(1L);
        assertSame(lessonResponseDTOList, actualFindByCourseIdResult);
        assertTrue(actualFindByCourseIdResult.isEmpty());
        verify(lessonRepository).findByCourseId(Mockito.<Long>any());
        verify(lessonMapper).toResponseDto(Mockito.<List<Lesson>>any());
    }

    /**
     * Method under test: {@link LessonServiceImpl#findByCourseId(Long)}
     */
    @Test
    void testFindByCourseId2() {
        when(lessonRepository.findByCourseId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(lessonMapper.toResponseDto(Mockito.<List<Lesson>>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> lessonServiceImpl.findByCourseId(1L));
        verify(lessonRepository).findByCourseId(Mockito.<Long>any());
        verify(lessonMapper).toResponseDto(Mockito.<List<Lesson>>any());
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

    /**
     * Method under test: {@link LessonServiceImpl#delete(Long)}
     */
    @Test
    void testDelete2() {
        doThrow(new SystemException(ErrorCode.OK)).when(lessonRepository).deleteById(Mockito.<Long>any());
        assertThrows(SystemException.class, () -> lessonServiceImpl.delete(1L));
        verify(lessonRepository).deleteById(Mockito.<Long>any());
    }
}

