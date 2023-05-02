package com.example.courseapi.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum used for describing user roles
 */
@RequiredArgsConstructor
@Getter
public enum CourseStatus {
    COMPLETED("Completed"),
    IN_PROGRESS("In progress"),
    FAILED("Failed");

    private final String title;
}
