package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.LessonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Lesson} and its DTO {@link LessonDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface LessonMapper extends EntityMapper<LessonDTO, Lesson> {

    @Mapping(source = "course.id", target = "courseId")
    LessonDTO toDto(Lesson lesson);

    @Mapping(source = "courseId", target = "course.id")
    Lesson toEntity(LessonDTO lessonDTO);

    default Lesson fromId(Long id) {
        if (id == null) {
            return null;
        }
        Lesson lesson = new Lesson();
        lesson.setId(id);
        return lesson;
    }
}
