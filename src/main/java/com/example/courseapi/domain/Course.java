package com.example.courseapi.domain;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

/**
 * Entity class for Course
 */
@Entity
@Table(name = "courses", schema = "course_management")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "title")
    private String title;

    @NotNull
    @Size(min = 10, max = 255)
    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(
            schema = "course_management",
            name = "courses_instructors",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    @Size(min = 1)
    private Set<Instructor> instructors;

    @ManyToMany
    @JoinTable(
            schema = "course_management",
            name = "courses_students",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Student> students;

    @OneToMany(mappedBy = "course")
    @Size(min = 5)
    private Set<Lesson> lessons;

    @OneToMany(mappedBy = "course")
    private Set<CourseFeedback> courseFeedbacks;

    public void addStudent(Student student) {
        this.students.add(student);
        student.getStudentCourses().add(this);
    }

    public void addInstructor(Instructor instructor) {
        this.instructors.add(instructor);
        instructor.getInstructorCourses().add(this);
    }
}
