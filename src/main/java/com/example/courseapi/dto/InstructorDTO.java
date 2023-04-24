package com.example.courseapi.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InstructorDTO extends BaseDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;
}
