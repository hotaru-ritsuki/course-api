package com.example.courseapi.service.impl;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.specs.SpecificationBuilder;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.request.LessonRequestDTO;
import com.example.courseapi.dto.response.LessonResponseDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.LessonRepository;
import com.example.courseapi.service.LessonService;
import com.example.courseapi.service.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Log4j2
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    public Optional<LessonResponseDTO> findById(final Long lessonId) {
        log.debug("Finding lesson with id: {}", lessonId);
        return lessonRepository.findById(lessonId).map(lessonMapper::toResponseDto);
    }

    @Override
    @Transactional
    public LessonResponseDTO save(final LessonRequestDTO lessonDTO) {
        log.debug("Saving lesson : {}", lessonDTO);
        if (Objects.nonNull(lessonDTO.getId())) {
            Lesson existingLesson = lessonRepository.findById(lessonDTO.getId()).orElseThrow(() ->
                    new SystemException("Lesson with id: " + lessonDTO.getId() + " not found.", ErrorCode.BAD_REQUEST));
            if (!lessonDTO.getCourseId().equals(existingLesson.getCourse().getId())
                    && existingLesson.getCourse().getAvailable()
                    && existingLesson.getCourse().getLessons().size() <= 5
            ) {
                throw new SystemException("Can not reassign course for lesson with id: " + lessonDTO.getId() + " due to course limitations",
                        ErrorCode.BAD_REQUEST);
            }
        }
        Lesson lesson = lessonMapper.fromRequestDto(lessonDTO);
        lesson = lessonRepository.save(lesson);

        return lessonMapper.toResponseDto(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LessonResponseDTO> findAll(final Filters filters, final Pageable pageable) {
        log.debug("Finding all lessons by filters and pageable");
        return lessonRepository.findAll(new SpecificationBuilder<Lesson>(filters).build(), pageable)
                .map(lessonMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponseDTO> findByCourseId(final Long courseId) {
        log.debug("Finding all lessons with course id: {}", courseId);
        return lessonMapper.toResponseDto(lessonRepository.findByCourseId(courseId));
    }

    @Override
    @Transactional
    public void delete(final Long lessonId) {
        log.debug("Deleting lesson with id: {}", lessonId);
        lessonRepository.deleteById(lessonId);
    }
}
