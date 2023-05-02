package com.example.courseapi.dto.response;

import com.example.courseapi.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionResponseDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -6270873599450127083L;
    
    private Double grade;
    private Long lessonId;
    private Long studentId;

    public String getEmbeddedIdsString() {
        return String.format("Lesson:%s.Student:%s", lessonId, studentId);
    }
}
