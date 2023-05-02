package com.example.courseapi.security.service.impl;

import com.example.courseapi.domain.Admin;
import com.example.courseapi.domain.Instructor;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.User;
import com.example.courseapi.exception.AccessValidationException;
import com.example.courseapi.repository.CourseRepository;
import com.example.courseapi.security.service.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccessValidator implements Validator {
    private final CourseRepository courseRepository;

    @Override
    public User getPrincipalOrThrow() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .orElseThrow(() -> new AccessValidationException("Current user is not present in Security Context."));
    }

    @Override
    public boolean courseAccess(final Long courseId, final Set<Long> instructorIds) {
        User principal = getPrincipalOrThrow();
        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Instructor instructor) {
            if (Objects.nonNull(courseId) && !courseRepository.existsByIdAndInstructorsId(courseId, instructor.getId())) {
                throw new AccessValidationException("Instructor is not assigned to the course.");
            } else if (CollectionUtils.isNotEmpty(instructorIds) && !instructorIds.contains(instructor.getId())) {
                throw new AccessValidationException("Instructor cannot unassign himself from course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean courseAccess(final Long courseId) {
        return courseAccess(courseId, null);
    }

    @Override
    public boolean courseFeedbackAccess(final Long courseId) {
        User principal = getPrincipalOrThrow();
        if (principal instanceof Student student) {
            if (!courseRepository.existsByIdAndStudentsId(courseId, student.getId())) {
                throw new AccessValidationException("Student is not subscribed to the course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean homeworkAccess(final Long lessonId, final Long studentId) {
        User principal = getPrincipalOrThrow();
        if (Objects.nonNull(studentId) && principal instanceof Admin) {
            return true;
        } else if (principal instanceof Student student) {
            if (Objects.nonNull(studentId) && !studentId.equals(student.getId())) {
                throw new AccessValidationException("Incorrect student id provided.");
            } else if (!courseRepository.existsByLessonsIdAndStudentsId(lessonId, student.getId())) {
                throw new AccessValidationException("Student is not subscribed to the course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean lessonAccess(final Long courseId) {
        return courseAccess(courseId, null);
    }

    @Override
    public boolean submissionAccess(final Long lessonId, final Long studentId) {
        User principal = getPrincipalOrThrow();
        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Instructor instructor) {
            if (!courseRepository.existsByLessonsIdAndInstructorsId(lessonId, instructor.getId())) {
                throw new AccessValidationException("Instructor is not assigned to the course.");
            }
            return true;
        } else if (Objects.nonNull(studentId) && principal instanceof Student student) {
            if (!studentId.equals(student.getId())) {
                throw new AccessValidationException("Student does not have permissions on requested lesson.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean submissionAccess(final Long lessonId) {
        return submissionAccess(lessonId, null);
    }
}
