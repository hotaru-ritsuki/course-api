package com.example.courseapi.service.impl;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.specs.SpecificationBuilder;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.LessonDTO;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.service.LessonService;
import com.example.courseapi.service.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    public Optional<LessonDTO> findById(Long id) {
        return lessonRepository.findById(id).map(lessonMapper::toDto);
    }

    @Override
    public LessonDTO save(LessonDTO lessonDTO) {
        Lesson lesson = lessonMapper.toEntity(lessonDTO);
        lesson = lessonRepository.save(lesson);

        return lessonMapper.toDto(lesson);
    }

    @Override
    public Page<LessonDTO> findAll(Filters filters, Pageable pageable) {
        return lessonRepository.findAll(new SpecificationBuilder<Lesson>(filters).build(), pageable)
                .map(lessonMapper::toDto);
    }

    @Override
    public List<LessonDTO> findByCourseId(Long lessonId) {
        return lessonMapper.toDto(lessonRepository.findByCourseId(lessonId));
    }

    @Override
    public void delete(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }
}
