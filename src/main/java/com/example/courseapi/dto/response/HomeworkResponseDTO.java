package com.example.courseapi.dto.response;

import com.example.courseapi.dto.BaseDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HomeworkResponseDTO extends BaseDTO {
    private Long id;
    private String title;
    private String filePath;
    private Long lessonId;
    private Long studentId;
}
