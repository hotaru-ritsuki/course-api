package com.example.courseapi.dto;

import com.example.courseapi.domain.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseGradeDTO {
    private CourseStatus courseStatus;
    private Double finalGrade;
}
