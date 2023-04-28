package com.example.courseapi.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class InstructorResponseDTO extends UserResponseDTO {
    private Set<Long> instructorCourseIds;
}
