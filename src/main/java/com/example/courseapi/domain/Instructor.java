package com.example.courseapi.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Instructor")
@DiscriminatorValue("INSTRUCTOR")
@Data
@ToString(exclude = "instructorCourses")
@EqualsAndHashCode(callSuper = true, exclude = "instructorCourses")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public final class Instructor extends User {

    @Builder.Default
    @ManyToMany
    @JoinTable(
            schema = "course_management",
            name = "courses_instructors",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> instructorCourses = new HashSet<>();
}
