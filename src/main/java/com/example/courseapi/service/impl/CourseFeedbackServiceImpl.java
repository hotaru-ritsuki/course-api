package com.example.courseapi.service.impl;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.specs.SpecificationBuilder;
import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.domain.Student;
import com.example.courseapi.dto.request.CourseFeedbackRequestDTO;
import com.example.courseapi.dto.response.CourseFeedbackResponseDTO;
import com.example.courseapi.repository.CourseFeedbackRepository;
import com.example.courseapi.service.CourseFeedbackService;
import com.example.courseapi.service.mapper.CourseFeedbackMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CourseFeedbackServiceImpl implements CourseFeedbackService {
    private final CourseFeedbackRepository courseFeedbackRepository;
    private final CourseFeedbackMapper courseFeedbackMapper;

    @Transactional(readOnly = true)
    public Optional<CourseFeedbackResponseDTO> findById(final Long id) {
        log.debug("Finding course feedback by id: {}", id);
        return courseFeedbackRepository.findById(id).map(courseFeedbackMapper::toResponseDto);
    }

    @Override
    @Transactional
    public CourseFeedbackResponseDTO save(final CourseFeedbackRequestDTO courseDTO, final Student student) {
        log.debug("Saving course feedback : {}", courseDTO);
        CourseFeedback course = courseFeedbackMapper.fromRequestDto(courseDTO);
        course.setStudent(student);
        course = courseFeedbackRepository.save(course);

        return courseFeedbackMapper.toResponseDto(course);
    }

    @Override
    public Page<CourseFeedbackResponseDTO> findAll(final Filters filters, final Pageable pageable) {
        log.debug("Finding all course feedbacks by filters and pageable");
        return courseFeedbackRepository.findAll(new SpecificationBuilder<CourseFeedback>(filters).build(), pageable)
                .map(courseFeedbackMapper::toResponseDto);
    }

    @Override
    public List<CourseFeedbackResponseDTO> findByStudentId(final Long studentId) {
        log.debug("Finding course feedback by student id: {}", studentId);
        return courseFeedbackMapper.toResponseDto(courseFeedbackRepository.findByStudentId(studentId));
    }

    @Override
    public List<CourseFeedbackResponseDTO> findByCourseId(final Long courseId) {
        log.debug("Finding course feedback by course id: {}", courseId);
        return courseFeedbackMapper.toResponseDto(courseFeedbackRepository.findByCourseId(courseId));
    }

    @Override
    @Transactional
    public void delete(final Long courseId) {
        log.debug("Deleting course feedback with id: {}", courseId);
        courseFeedbackRepository.deleteById(courseId);
    }
}
