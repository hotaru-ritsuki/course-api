package com.example.courseapi.security.service;

import com.example.courseapi.domain.User;

import java.util.Set;

public interface Validator {
    User getPrincipalOrThrow();

    boolean courseAccess(final Long courseId, final Set<Long> instructorIds);

    boolean courseAccess(final Long courseId);

    boolean courseFeedbackAccess(final Long courseId);

    boolean homeworkAccess(final Long lessonId, final Long studentId);

    boolean lessonAccess(final Long courseId);

    boolean submissionAccess(final Long lessonId, final Long studentId);

    boolean submissionAccess(final Long lessonId);
}
