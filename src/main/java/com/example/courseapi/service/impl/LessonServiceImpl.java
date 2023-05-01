package com.example.courseapi.service.impl;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.specs.SpecificationBuilder;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.domain.User;
import com.example.courseapi.dto.request.LessonRequestDTO;
import com.example.courseapi.dto.response.LessonResponseDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.service.LessonService;
import com.example.courseapi.service.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    public Optional<LessonResponseDTO> findById(Long id) {
        return lessonRepository.findById(id).map(lessonMapper::toResponseDto);
    }

    @Override
    @Transactional
    public LessonResponseDTO save(LessonRequestDTO lessonRequestDTO) {
        if (Objects.nonNull(lessonRequestDTO.getId())) {
            Lesson existingLesson = lessonRepository.findById(lessonRequestDTO.getId()).orElseThrow(() ->
                    new SystemException("Lesson with id: " + lessonRequestDTO.getId() + " not found.", ErrorCode.BAD_REQUEST));
            if (!lessonRequestDTO.getCourseId().equals(existingLesson.getCourse().getId())
                    && existingLesson.getCourse().getAvailable()
                    && existingLesson.getCourse().getLessons().size() <= 5
            ) {
                throw new SystemException("Can not reassign course for lesson with id: " + lessonRequestDTO.getId() + " due to course limitations",
                        ErrorCode.BAD_REQUEST);
            }
        }
        Lesson lesson = lessonMapper.fromRequestDto(lessonRequestDTO);
        lesson = lessonRepository.save(lesson);

        return lessonMapper.toResponseDto(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LessonResponseDTO> findAll(Filters filters, Pageable pageable) {
        return lessonRepository.findAll(new SpecificationBuilder<Lesson>(filters).build(), pageable)
                .map(lessonMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponseDTO> findByCourseId(Long lessonId) {
        return lessonMapper.toResponseDto(lessonRepository.findByCourseId(lessonId));
    }

    @Override
    @Transactional
    public void delete(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }
}
