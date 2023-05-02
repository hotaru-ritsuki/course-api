package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Submission;
import com.example.courseapi.dto.request.SubmissionRequestDTO;
import com.example.courseapi.dto.response.SubmissionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Submission} and its DTO {@link SubmissionResponseDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface SubmissionMapper extends EntityMapper<SubmissionRequestDTO, SubmissionResponseDTO, Submission> {

    @Mappings({
            @Mapping(source = "student.id", target = "studentId"),
            @Mapping(source = "lesson.id", target = "lessonId"),
    })
    SubmissionResponseDTO toResponseDto(final Submission submission);

    @Mappings({
            @Mapping(source = "studentId", target = "student.id"),
            @Mapping(source = "studentId", target = "submissionId.studentId"),
            @Mapping(source = "lessonId", target = "lesson.id"),
            @Mapping(source = "lessonId", target = "submissionId.lessonId"),
    })
    Submission fromResponseDto(final SubmissionResponseDTO submissionResponseDTO);

    @Mappings({
            @Mapping(source = "student.id", target = "studentId"),
            @Mapping(source = "lesson.id", target = "lessonId"),
    })
    SubmissionRequestDTO toRequestDto(final Submission submission);

    @Mappings({
            @Mapping(source = "studentId", target = "student.id"),
            @Mapping(source = "studentId", target = "submissionId.studentId"),
            @Mapping(source = "lessonId", target = "lesson.id"),
            @Mapping(source = "lessonId", target = "submissionId.lessonId"),
    })
    Submission fromRequestDto(final SubmissionRequestDTO submissionRequestDTO);
}

