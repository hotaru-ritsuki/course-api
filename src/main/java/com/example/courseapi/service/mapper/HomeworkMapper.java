package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Homework;
import com.example.courseapi.domain.Lesson;
import com.example.courseapi.dto.HomeworkDTO;
import com.example.courseapi.dto.LessonDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Homework} and its DTO {@link HomeworkDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface HomeworkMapper extends EntityMapper<HomeworkDTO, Homework> {
    HomeworkDTO toDto(Homework homework);

    Homework toEntity(HomeworkDTO homeworkDTO);
}
