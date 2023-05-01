package com.example.courseapi.dto.response;

import com.example.courseapi.dto.BaseDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * A DTO for the {@link com.example.courseapi.domain.Course} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class CourseResponseDTO extends BaseDTO {

    protected Long id;
    protected String title;
    protected String description;
    protected Boolean available;
    protected Set<Long> instructorIds;
    protected Set<Long> studentIds;
    protected Set<LessonResponseDTO> lessons;
}
