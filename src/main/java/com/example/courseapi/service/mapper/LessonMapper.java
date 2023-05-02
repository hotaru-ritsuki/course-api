package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.request.LessonRequestDTO;
import com.example.courseapi.dto.response.LessonResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Lesson} and its DTO {@link LessonResponseDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface LessonMapper extends EntityMapper<LessonRequestDTO, LessonResponseDTO, Lesson> {

    @Mapping(source = "course.id", target = "courseId")
    LessonResponseDTO toResponseDto(final Lesson lesson);

    @Mapping(source = "courseId", target = "course.id")
    Lesson fromResponseDto(final LessonResponseDTO lessonResponseDTO);

    @Mapping(source = "course.id", target = "courseId")
    LessonRequestDTO toRequestDto(final Lesson lesson);

    @Mapping(source = "courseId", target = "course.id")
    Lesson fromRequestDto(final LessonRequestDTO lessonRequestDTO);

    default Lesson fromId(Long id) {
        if (id == null) {
            return null;
        }
        Lesson lesson = new Lesson();
        lesson.setId(id);
        return lesson;
    }
}
