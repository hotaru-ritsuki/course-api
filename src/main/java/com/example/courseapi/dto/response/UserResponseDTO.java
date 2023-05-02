package com.example.courseapi.dto.response;

import com.example.courseapi.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;


@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponseDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -5424120918420193475L;

    protected Long id;
    protected String firstName;
    protected String lastName;
    protected String email;
}
