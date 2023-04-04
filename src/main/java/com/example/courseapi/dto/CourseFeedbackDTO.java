package com.example.courseapi.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseFeedbackDTO extends BaseDTO {
    private Long id;

    private String feedback;

    private CourseDTO course;

    private UserDTO student;
}