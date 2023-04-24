package com.example.courseapi.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity(name = "Instructor")
@DiscriminatorValue("INSTRUCTOR")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Instructor extends User {

    @ManyToMany(mappedBy = "instructors")
    private Set<Course> instructorCourses;
}
