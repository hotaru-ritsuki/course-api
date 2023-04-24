package com.example.courseapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity(name = "Student")
@DiscriminatorValue("STUDENT")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Student extends User {

    @Size(max = 5)
    @ManyToMany(mappedBy = "students")
    private Set<Course> studentCourses;

    @OneToMany(mappedBy = "student")
    private Set<CourseFeedback> courseFeedbacks;
}
