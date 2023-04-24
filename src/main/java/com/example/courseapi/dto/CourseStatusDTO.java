package com.example.courseapi.dto;

import com.example.courseapi.domain.enums.CourseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A DTO for the {@link com.example.courseapi.domain.Course} entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseStatusDTO extends CourseDTO {

    private CourseStatus courseStatus;
}
