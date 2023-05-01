package com.example.courseapi.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionRequestDTO {

    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    private Double grade;

    @NotNull
    private Long lessonId;

    @NotNull
    private Long studentId;
}
