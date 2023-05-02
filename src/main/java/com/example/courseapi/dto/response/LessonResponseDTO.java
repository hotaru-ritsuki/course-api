package com.example.courseapi.dto.response;

import com.example.courseapi.dto.BaseDTO;
import lombok.*;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonResponseDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -7716873925492743870L;
    
    private Long id;
    private String title;
    private String description;
    private Long courseId;
}
