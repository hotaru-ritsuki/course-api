package com.example.courseapi.dto.response;

import com.example.courseapi.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CourseFeedbackResponseDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -344133257513053075L;

    private Long id;
    private String feedback;
    private Long courseId;
    private Long studentId;
}