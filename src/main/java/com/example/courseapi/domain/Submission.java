package com.example.courseapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

import static java.util.Objects.isNull;

/**
 * Entity class for Submission
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "submissions", schema = "course_management")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Submission extends BaseEntity {

    @EmbeddedId
    private SubmissionId submissionId = new SubmissionId();

    @NotNull
    @Column(name = "grade")
    private Double grade;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId(value = "studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId(value = "lessonId")
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubmissionId implements Serializable {
        @Serial
        private static final long serialVersionUID = -8386131251581614960L;
        
        @Column(name = "student_id")
        public Long studentId;

        @Column(name = "lesson_id")
        public Long lessonId;
    }

    public abstract static class SubmissionBuilder<C extends Submission, B extends SubmissionBuilder<C, B>>
            extends BaseEntityBuilder<C,B> {
        public SubmissionBuilder<C, B> student(Student student) {
            this.student = student;
            if (isNull(this.submissionId)) {
                this.submissionId = new SubmissionId();
            }
            this.submissionId.setStudentId(student.getId());
            return self();
        }

        public SubmissionBuilder<C, B> lesson(Lesson lesson) {
            this.lesson = lesson;
            if (isNull(this.submissionId)) {
                this.submissionId = new SubmissionId();
            }
            this.submissionId.setLessonId(lesson.getId());
            return self();
        }
    }
}
