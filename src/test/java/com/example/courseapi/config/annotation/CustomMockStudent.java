package com.example.courseapi.config.annotation;

import com.example.courseapi.config.support.CustomMockStudentSecurityContextFactory;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = CustomMockStudentSecurityContextFactory.class, setupBefore = TestExecutionEvent.TEST_EXECUTION)
public @interface CustomMockStudent {}
