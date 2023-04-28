package com.example.courseapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Student")
@DiscriminatorValue("STUDENT")
@Data
@ToString(exclude = {"studentCourses"})
@EqualsAndHashCode(callSuper = true, exclude = {"studentCourses"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Student extends User {

    @Size(max = 5)
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            schema = "course_management",
            name = "courses_students",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> studentCourses = new HashSet<>();
}
