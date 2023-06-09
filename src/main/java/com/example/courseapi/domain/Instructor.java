package com.example.courseapi.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Instructor")
@DiscriminatorValue("INSTRUCTOR")
@EntityListeners(AuditingEntityListener.class)
@Data
@ToString(exclude = "instructorCourses")
@EqualsAndHashCode(callSuper = true, exclude = "instructorCourses")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public final class Instructor extends User {
    @Serial
    private static final long serialVersionUID = -582900698897846648L;

    @Builder.Default
    @ManyToMany(mappedBy = "instructors", fetch = FetchType.EAGER)
    private Set<Course> instructorCourses = new HashSet<>();

    @PreRemove
    private void deleteRelations() {
        for (Course course : this.instructorCourses) {
            this.removeInstructorCourse(course);
        }
    }

    public void addInstructorCourse(Course course) {
        this.instructorCourses.add(course);
        course.getInstructors().add(this);
    }

    public void removeInstructorCourse(Course course) {
        this.instructorCourses.remove(course);
        course.getInstructors().remove(this);
    }
}
