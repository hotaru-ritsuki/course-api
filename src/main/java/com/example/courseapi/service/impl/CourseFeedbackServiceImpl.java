package com.example.courseapi.service.impl;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.specs.SpecificationBuilder;
import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.dto.CourseFeedbackDTO;
import com.example.courseapi.repository.CourseFeedbackRepository;
import com.example.courseapi.service.CourseFeedbackService;
import com.example.courseapi.service.mapper.CourseFeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CourseFeedbackServiceImpl implements CourseFeedbackService {
    private final CourseFeedbackRepository courseFeedbackRepository;
    private final CourseFeedbackMapper courseFeedbackMapper;

    public Optional<CourseFeedbackDTO> findById(Long id) {
        return courseFeedbackRepository.findById(id).map(courseFeedbackMapper::toDto);
    }

    @Override
    public CourseFeedbackDTO save(CourseFeedbackDTO courseDTO) {
        CourseFeedback course = courseFeedbackMapper.toEntity(courseDTO);
        course = courseFeedbackRepository.save(course);

        return courseFeedbackMapper.toDto(course);
    }

    @Override
    public Page<CourseFeedbackDTO> findAll(Filters filters, Pageable pageable) {
        return courseFeedbackRepository.findAll(new SpecificationBuilder<CourseFeedback>(filters).build(), pageable)
                .map(courseFeedbackMapper::toDto);
    }

    @Override
    public List<CourseFeedbackDTO> findByStudentId(Long studentId) {
        return courseFeedbackMapper.toDto(courseFeedbackRepository.findByStudentId(studentId));
    }

    @Override
    public List<CourseFeedbackDTO> findByCourseId(Long courseId) {
        return courseFeedbackMapper.toDto(courseFeedbackRepository.findByCourseId(courseId));
    }

    @Override
    public void delete(Long courseId) {
        courseFeedbackRepository.deleteById(courseId);
    }
}
