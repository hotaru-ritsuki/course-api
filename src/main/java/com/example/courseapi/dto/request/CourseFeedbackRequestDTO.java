package com.example.courseapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseFeedbackRequestDTO {
    private Long id;

    @NotNull
    @Size(min = 10, max = 255)
    private String feedback;

    @NotNull
    private Long courseId;
}