package com.example.courseapi.security.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.courseapi.exception.AccessValidationException;
import com.example.courseapi.repository.CourseRepository;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AccessValidator.class})
@ExtendWith(SpringExtension.class)
class AccessValidatorTest {
    @Autowired
    private AccessValidator accessValidator;

    @MockBean
    private CourseRepository courseRepository;

    /**
     * Method under test: {@link AccessValidator#getPrincipalOrThrow()}
     */
    @Test
    void testGetPrincipalOrThrow() {
        assertThrows(AccessValidationException.class, () -> accessValidator.getPrincipalOrThrow());
    }

    /**
     * Method under test: {@link AccessValidator#courseAccess(Long, Set)}
     */
    @Test
    void testCourseAccess() {
        assertThrows(AccessValidationException.class, () -> accessValidator.courseAccess(1L, new HashSet<>()));
    }

    /**
     * Method under test: {@link AccessValidator#courseFeedbackAccess(Long)}
     */
    @Test
    void testCourseFeedbackAccess() {
        assertThrows(AccessValidationException.class, () -> accessValidator.courseFeedbackAccess(1L));
    }

    /**
     * Method under test: {@link AccessValidator#homeworkAccess(Long, Long)}
     */
    @Test
    void testHomeworkAccess() {
        assertThrows(AccessValidationException.class, () -> accessValidator.homeworkAccess(1L, 1L));
    }

    /**
     * Method under test: {@link AccessValidator#lessonAccess(Long)}
     */
    @Test
    void testLessonAccess() {
        assertThrows(AccessValidationException.class, () -> accessValidator.lessonAccess(1L));
    }

    /**
     * Method under test: {@link AccessValidator#submissionAccess(Long)}
     */
    @Test
    void testSubmissionAccess() {
        assertThrows(AccessValidationException.class, () -> accessValidator.submissionAccess(1L));
        assertThrows(AccessValidationException.class, () -> accessValidator.submissionAccess(1L, 1L));
    }
}

