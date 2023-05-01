package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.dto.request.CourseFeedbackRequestDTO;
import com.example.courseapi.dto.response.CourseFeedbackResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link CourseFeedback} and its DTO {@link CourseFeedbackResponseDTO}.
 */
@Mapper(componentModel = "spring", uses = {StudentMapper.class, CourseMapper.class})
@Component
public interface CourseFeedbackMapper extends EntityMapper<CourseFeedbackRequestDTO, CourseFeedbackResponseDTO, CourseFeedback> {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "course.id", target = "courseId")
    CourseFeedbackResponseDTO toResponseDto(CourseFeedback courseFeedback);

    @Mapping(source = "studentId", target = "student")
    @Mapping(source = "courseId", target = "course")
    CourseFeedback fromResponseDto(CourseFeedbackResponseDTO courseFeedbackResponseDTO);

    @Mapping(source = "courseId", target = "course")
    CourseFeedback fromRequestDto(CourseFeedbackRequestDTO courseFeedbackRequestDTO);

    @Mapping(source = "course.id", target = "courseId")
    CourseFeedbackRequestDTO toRequestDto(CourseFeedback courseFeedback);

    default CourseFeedback fromId(Long id) {
        if (id == null) {
            return null;
        }
        CourseFeedback courseFeedback = new CourseFeedback();
        courseFeedback.setId(id);
        return courseFeedback;
    }
}
