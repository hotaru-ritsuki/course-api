package com.example.courseapi.dto.response;

import com.example.courseapi.dto.BaseDTO;
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
public class SubmissionResponseDTO extends BaseDTO {
    private Double grade;
    private Long lessonId;
    private Long studentId;

    public String getEmbeddedIdsString() {
        return String.format("Lesson:%s.Student:%s", lessonId, studentId);
    }
}
