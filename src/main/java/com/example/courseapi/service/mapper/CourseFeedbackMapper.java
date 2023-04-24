package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.dto.CourseFeedbackDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link CourseFeedback} and its DTO {@link CourseFeedbackDTO}.
 */
@Mapper(componentModel = "spring", uses = {StudentMapper.class, CourseMapper.class})
@Component
public interface CourseFeedbackMapper extends EntityMapper<CourseFeedbackDTO, CourseFeedback> {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "course.id", target = "courseId")
    CourseFeedbackDTO toDto(CourseFeedback courseFeedback);

    @Mapping(source = "studentId", target = "student")
    @Mapping(source = "courseId", target = "course")
    CourseFeedback toEntity(CourseFeedbackDTO courseFeedbackDTO);

    default CourseFeedback fromId(Long id) {
        if (id == null) {
            return null;
        }
        CourseFeedback courseFeedback = new CourseFeedback();
        courseFeedback.setId(id);
        return courseFeedback;
    }
}
