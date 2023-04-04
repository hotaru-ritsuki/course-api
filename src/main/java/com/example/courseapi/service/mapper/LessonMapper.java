package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.CourseDTO;
import com.example.courseapi.dto.LessonDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Lesson} and its DTO {@link LessonDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface LessonMapper extends EntityMapper<LessonDTO, Lesson> {

    LessonDTO toDto(Lesson lesson);

    Lesson toEntity(LessonDTO lessonDTO);
}
