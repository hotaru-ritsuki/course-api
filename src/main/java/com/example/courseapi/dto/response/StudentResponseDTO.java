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
public class StudentResponseDTO extends UserResponseDTO {
    @Serial
    private static final long serialVersionUID = -7445789521398418054L;
    
    private Set<Long> studentCourseIds;
}
