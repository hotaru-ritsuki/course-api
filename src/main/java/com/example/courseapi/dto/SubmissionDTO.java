package com.example.courseapi.dto;

import com.example.courseapi.domain.Lesson;
import com.example.courseapi.domain.Submission;
import com.example.courseapi.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubmissionDTO extends BaseDTO {

    private Double grade;

    private Long lessonId;

    private Long studentId;

    public String getEmbeddedIdsString() {
        return String.format("Lesson:%s.Student:%s", lessonId, studentId);
    }
}
