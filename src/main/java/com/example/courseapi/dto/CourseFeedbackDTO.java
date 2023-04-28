package com.example.courseapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseFeedbackDTO extends BaseDTO {
    private Long id;

    @NotNull
    @Size(min = 10, max = 255)
    private String feedback;

    @NotNull
    private Long courseId;

    @NotNull
    private Long studentId;
}