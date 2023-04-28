package com.example.courseapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponseDTO extends BaseDTO {

    protected Long id;

    @NotNull
    @Size(min = 2, max = 50)
    protected String firstName;

    @NotNull
    @Size(min = 2, max = 50)
    protected String lastName;

    @NotNull
    @Email
    protected String email;
}
