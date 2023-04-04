package com.example.courseapi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "submissions", schema = "course_management")
@Data
@EqualsAndHashCode(callSuper = true)
public class Submission extends BaseEntity {

    @EmbeddedId
    private SubmissionId submissionId;

    @NotNull
    @Column(name = "grade")
    private Double grade;

    @NotNull
    @ManyToOne
    @MapsId(value = "studentId")
    @JoinColumn(name = "student_id")
    private User student;

    @NotNull
    @ManyToOne
    @MapsId(value = "lessonId")
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Data
    @Embeddable
    public static class SubmissionId implements Serializable {

        @Column(name = "student_id")
        private Long studentId;

        @Column(name = "lesson_id")
        private Long lessonId;
    }
}
