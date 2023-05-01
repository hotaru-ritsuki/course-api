package com.example.courseapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * A DTO for the {@link com.example.courseapi.domain.Course} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDTO {

    protected Long id;

    @NotNull
    @Size(min = 2, max = 100)
    protected String title;

    @NotNull
    @Size(min = 10, max = 255)
    protected String description;

    @NotNull
    @Size(min = 1)
    protected Set<Long> instructorIds;

    protected Set<Long> studentIds;
}
