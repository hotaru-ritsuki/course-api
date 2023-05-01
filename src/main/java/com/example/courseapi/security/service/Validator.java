package com.example.courseapi.security.service;

import java.util.Set;

public interface Validator {
    boolean courseAccess(Long courseId, Set<Long> instructorIds);

    boolean courseFeedbackAccess(Long courseId);

    boolean homeworkAccess(Long lessonId, Long studentId);

    boolean lessonAccess(Long courseId);

    boolean submissionAccess(Long lessonId, Long studentId);

    boolean submissionAccess(Long lessonId);
}
