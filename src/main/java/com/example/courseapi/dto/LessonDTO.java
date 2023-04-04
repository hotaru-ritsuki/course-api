package com.example.courseapi.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LessonDTO extends BaseDTO {
    private Long id;

    private String title;

    private String description;
}
