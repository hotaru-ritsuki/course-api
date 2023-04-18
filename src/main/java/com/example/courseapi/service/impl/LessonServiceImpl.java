package com.example.courseapi.service.impl;

import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.LessonDTO;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.service.LessonService;
import com.example.courseapi.service.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
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
    public List<LessonDTO> findAll() {
        return lessonMapper.toDto(lessonRepository.findAll());
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
