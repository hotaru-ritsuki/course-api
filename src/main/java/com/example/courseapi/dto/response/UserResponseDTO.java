package com.example.courseapi.dto.response;

import com.example.courseapi.dto.BaseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponseDTO extends BaseDTO {

    protected Long id;
    protected String firstName;
    protected String lastName;
    protected String email;
}
