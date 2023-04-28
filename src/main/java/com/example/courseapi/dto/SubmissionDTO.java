package com.example.courseapi.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDTO extends BaseDTO {

    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    private Double grade;

    @NotNull
    private Long lessonId;

    @NotNull
    private Long studentId;

    public String getEmbeddedIdsString() {
        return String.format("Lesson:%s.Student:%s", lessonId, studentId);
    }
}
