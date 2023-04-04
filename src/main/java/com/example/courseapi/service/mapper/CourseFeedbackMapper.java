package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.CourseFeedback;
import com.example.courseapi.dto.CourseFeedbackDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link CourseFeedback} and its DTO {@link CourseFeedbackDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface CourseFeedbackMapper extends EntityMapper<CourseFeedbackDTO, CourseFeedback> {

    CourseFeedbackDTO toDto(CourseFeedback courseFeedback);

    CourseFeedback toEntity(CourseFeedbackDTO courseFeedbackDTO);
}
