package com.example.courseapi.dto.response;

import com.example.courseapi.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class HomeworkResponseDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -6955380971749684668L;

    private Long id;
    private String title;
    private String filePath;
    private Long lessonId;
    private Long studentId;
}
