package com.example.courseapi.dto.response;

import com.example.courseapi.domain.enums.CourseStatus;
import com.example.courseapi.dto.response.CourseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.example.courseapi.domain.Course} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CourseStatusResponseDTO extends CourseResponseDTO {

    private CourseStatus courseStatus;
    private Double finalGrade;
}
