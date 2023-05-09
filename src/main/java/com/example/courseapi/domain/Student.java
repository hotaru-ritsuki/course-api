package com.example.courseapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Student")
@DiscriminatorValue("STUDENT")
@EntityListeners(AuditingEntityListener.class)
@Data
@ToString(exclude = {"studentCourses"})
@EqualsAndHashCode(callSuper = true, exclude = {"studentCourses"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Student extends User {

    @Serial
    private static final long serialVersionUID = 808391021538769642L;
    
    @Size(max = 5)
    @Builder.Default
    @ManyToMany(mappedBy = "students", fetch = FetchType.EAGER)
    private Set<Course> studentCourses = new HashSet<>();

    @PreRemove
    private void deleteRelations() {
        for (Course course : this.studentCourses) {
            this.removeStudentCourse(course);
        }
    }

    public void addStudentCourse(Course course) {
        this.studentCourses.add(course);
        course.getStudents().add(this);
    }

    public void removeStudentCourse(Course course) {
        this.studentCourses.remove(course);
        course.getStudents().remove(this);
    }
}
