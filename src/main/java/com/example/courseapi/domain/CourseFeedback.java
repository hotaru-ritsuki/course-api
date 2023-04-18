package com.example.courseapi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Entity class for Course Feedback
 */
@Entity
@Table(name = "course_feedbacks", schema = "course_management")
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseFeedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_feedback_id")
    private Long id;

    @Column(name = "feedback")
    private String feedback;

    @NotNull
    @ManyToOne
    private Course course;

    @NotNull
    @ManyToOne
    private User student;
}
