package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Homework;
import com.example.courseapi.dto.request.HomeworkRequestDTO;
import com.example.courseapi.dto.response.HomeworkResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Homework} and its DTO {@link HomeworkResponseDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface HomeworkMapper extends EntityMapper<HomeworkRequestDTO, HomeworkResponseDTO, Homework> {

    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "student.id", target = "studentId")
    HomeworkResponseDTO toResponseDto(Homework homework);

    @Mapping(source = "lessonId", target = "lesson.id")
    @Mapping(source = "studentId", target = "student.id")
    Homework fromResponseDto(HomeworkResponseDTO homeworkResponseDTO);


    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "student.id", target = "studentId")
    HomeworkRequestDTO toRequestDto(Homework homework);

    @Mapping(source = "lessonId", target = "lesson.id")
    @Mapping(source = "studentId", target = "student.id")
    Homework fromRequestDto(HomeworkRequestDTO homeworkRequestDTO);

    default Homework fromId(Long id) {
        if (id == null) {
            return null;
        }
        Homework homework = new Homework();
        homework.setId(id);
        return homework;
    }
}
