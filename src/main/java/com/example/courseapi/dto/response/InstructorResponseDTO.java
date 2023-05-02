package com.example.courseapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InstructorResponseDTO extends UserResponseDTO {
    @Serial
    private static final long serialVersionUID = -6525162897131691238L;
    
    private Set<Long> instructorCourseIds;
}
