package com.example.courseapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * A DTO for the {@link com.example.courseapi.domain.Course} entity.
 */
@Data
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class CourseDTO extends BaseDTO {

    protected Long id;

    @NotNull
    @Size(min = 2, max = 100)
    protected String title;

    @NotNull
    @Size(min = 10, max = 255)
    protected String description;

    protected Set<Long> instructorIds;

    protected Set<Long> studentIds;

    @Size(min = 5)
    protected Set<Long> lessonIds;
}
