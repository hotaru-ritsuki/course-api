package com.example.courseapi.service.impl;

import com.example.courseapi.domain.Course;
import com.example.courseapi.dto.CourseDTO;
import com.example.courseapi.repository.CourseRepository;
import com.example.courseapi.service.CourseService;
import com.example.courseapi.service.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public Optional<CourseDTO> findById(Long id) {
        return courseRepository.findById(id).map(courseMapper::toDto);
    }

    @Override
    public CourseDTO save(CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);
        course = courseRepository.save(course);

        return courseMapper.toDto(course);
    }

    @Override
    public List<CourseDTO> findAll() {
        return courseMapper.toDto(courseRepository.findAll());
    }

    @Override
    public List<CourseDTO> findByStudentId(Long studentId) {
        return courseMapper.toDto(courseRepository.findByStudentsId(studentId));
    }

    @Override
    public void delete(Long courseId) {
        courseRepository.deleteById(courseId);
    }
}
