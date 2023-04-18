package com.example.courseapi.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HomeworkDTO extends BaseDTO {
    private Long id;

    private String title;

    private String filePath;

    private Long lessonId;

    private Long studentId;
}
