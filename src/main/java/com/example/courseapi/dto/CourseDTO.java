package com.example.courseapi.dto;

import com.example.courseapi.domain.Lesson;
import com.example.courseapi.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * A DTO for the {@link com.example.courseapi.domain.Course} entity.
 */
@Data
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class CourseDTO extends BaseDTO {

    private Long id;

    private String name;

    private String description;

    private Set<User> instructors;

    private Set<User> students;

    private Set<Lesson> lessons;
}
