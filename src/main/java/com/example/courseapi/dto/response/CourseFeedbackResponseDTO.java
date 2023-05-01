package com.example.courseapi.dto.response;

import com.example.courseapi.dto.BaseDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CourseFeedbackResponseDTO extends BaseDTO {
    private Long id;
    private String feedback;
    private Long courseId;
    private Long studentId;
}