package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Homework;
import com.example.courseapi.dto.HomeworkDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Homework} and its DTO {@link HomeworkDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface HomeworkMapper extends EntityMapper<HomeworkDTO, Homework> {

    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "student.id", target = "studentId")
    HomeworkDTO toDto(Homework homework);

    @Mapping(source = "lessonId", target = "lesson.id")
    @Mapping(source = "studentId", target = "student.id")
    Homework toEntity(HomeworkDTO homeworkDTO);

    default Homework fromId(Long id) {
        if (id == null) {
            return null;
        }
        Homework homework = new Homework();
        homework.setId(id);
        return homework;
    }
}
