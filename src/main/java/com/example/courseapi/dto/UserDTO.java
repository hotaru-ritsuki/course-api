package com.example.courseapi.dto;

import com.example.courseapi.domain.enums.Roles;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Roles role;
}
