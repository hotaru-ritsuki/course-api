package com.example.courseapi.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Roles {
    ADMIN("Administrator"),
    INSTRUCTOR("Instructor"),
    STUDENT("Student");

    private final String name;
}