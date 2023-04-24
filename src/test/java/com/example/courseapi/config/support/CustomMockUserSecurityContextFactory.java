package com.example.courseapi.config.support;

import com.example.courseapi.config.annotation.CustomMockUser;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class CustomMockUserSecurityContextFactory implements WithSecurityContextFactory<CustomMockUser> {

    @Override
    public SecurityContext createSecurityContext(CustomMockUser customMockUser) {
        return SecurityContextHolder.createEmptyContext();
    }
}
