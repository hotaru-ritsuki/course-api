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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CourseFeedbackServiceImpl implements CourseFeedbackService {
    private final CourseFeedbackRepository courseFeedbackRepository;
    private final CourseFeedbackMapper courseFeedbackMapper;

    @Transactional(readOnly = true)
    public Optional<CourseFeedbackResponseDTO> findById(Long id) {
        return courseFeedbackRepository.findById(id).map(courseFeedbackMapper::toResponseDto);
    }

    @Override
    @Transactional
    public CourseFeedbackResponseDTO save(CourseFeedbackRequestDTO courseDTO, Student student) {
        CourseFeedback course = courseFeedbackMapper.fromRequestDto(courseDTO);
        course.setStudent(student);
        course = courseFeedbackRepository.save(course);

        return courseFeedbackMapper.toResponseDto(course);
    }

    @Override
    public Page<CourseFeedbackResponseDTO> findAll(Filters filters, Pageable pageable) {
        return courseFeedbackRepository.findAll(new SpecificationBuilder<CourseFeedback>(filters).build(), pageable)
                .map(courseFeedbackMapper::toResponseDto);
    }

    @Override
    public List<CourseFeedbackResponseDTO> findByStudentId(Long studentId) {
        return courseFeedbackMapper.toResponseDto(courseFeedbackRepository.findByStudentId(studentId));
    }

    @Override
    public List<CourseFeedbackResponseDTO> findByCourseId(Long courseId) {
        return courseFeedbackMapper.toResponseDto(courseFeedbackRepository.findByCourseId(courseId));
    }

    @Override
    @Transactional
    public void delete(Long courseId) {
        courseFeedbackRepository.deleteById(courseId);
    }
}
