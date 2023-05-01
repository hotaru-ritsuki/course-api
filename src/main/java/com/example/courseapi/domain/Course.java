package com.example.courseapi.domain;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class for Course
 */
@Entity
@Table(name = "courses", schema = "course_management")
@Data
@ToString(exclude = {"instructors", "students", "lessons", "courseFeedbacks"})
@EqualsAndHashCode(callSuper = true, exclude = {"instructors", "students", "lessons", "courseFeedbacks"})
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "title")
    private String title;

    @NotNull
    @Size(min = 10, max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Builder.Default
    @Column(name = "available")
    private Boolean available = false;

    @Size(min = 1)
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            schema = "course_management",
            name = "courses_instructors",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "instructor_id"))
    private Set<Instructor> instructors = new HashSet<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            schema = "course_management",
            name = "courses_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> students = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Lesson> lessons = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "course")
    private Set<CourseFeedback> courseFeedbacks = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void validateCourse() {
        this.available = CollectionUtils.sizeIsEmpty(this.instructors)
                && CollectionUtils.size(this.lessons) >= 5;
    }

    @PreRemove
    private void deleteRelations() {
        for (Instructor instructor : this.instructors) {
            this.removeInstructor(instructor);
        }
    }

    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.setCourse(this);
    }

    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.setCourse(null);
    }

    public void addStudent(Student student) {
        this.students.add(student);
        student.getStudentCourses().add(this);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getStudentCourses().remove(this);
    }

    public void addInstructor(Instructor instructor) {
        this.instructors.add(instructor);
        instructor.getInstructorCourses().add(this);
    }

    public void removeInstructor(Instructor instructor) {
        this.instructors.remove(instructor);
        instructor.getInstructorCourses().remove(this);
    }
}
