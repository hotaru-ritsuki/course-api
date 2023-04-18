package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Submission;
import com.example.courseapi.dto.SubmissionDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Submission} and its DTO {@link SubmissionDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface SubmissionMapper extends EntityMapper<SubmissionDTO, Submission> {

    SubmissionDTO toDto(Submission lesson);

    Submission toEntity(SubmissionDTO lessonDTO);
}

