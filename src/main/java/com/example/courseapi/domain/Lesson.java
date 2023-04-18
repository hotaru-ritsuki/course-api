package com.example.courseapi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * Entity class for Lesson
 */
@Entity
@Table(name = "lessons", schema = "course_management")
@Data
@EqualsAndHashCode(callSuper = true)
public class Lesson extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
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
    @ManyToOne
    private Course course;

    @OneToMany(mappedBy = "lesson")
    private Set<Submission> submissions;
}
