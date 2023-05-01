package com.example.courseapi.dto.response;

import com.example.courseapi.dto.BaseDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonResponseDTO extends BaseDTO {
    private Long id;
    private String title;
    private String description;
    private Long courseId;
}
