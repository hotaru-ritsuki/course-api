package com.example.courseapi.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentResponseDTO extends UserResponseDTO {
    private Set<Long> studentCourseIds;
}
