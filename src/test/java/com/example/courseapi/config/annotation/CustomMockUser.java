package com.example.courseapi.config.annotation;

import com.example.courseapi.config.support.CustomMockUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = CustomMockUserSecurityContextFactory.class)
public @interface CustomMockUser {}
