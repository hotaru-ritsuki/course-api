package com.example.courseapi.config.support;

import com.example.courseapi.config.annotation.CustomMockInstructor;
import com.example.courseapi.domain.Instructor;
import com.example.courseapi.repository.InstructorRepository;
import com.example.courseapi.util.EntityCreatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomMockInstructorSecurityContextFactory implements WithSecurityContextFactory<CustomMockInstructor> {

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public SecurityContext createSecurityContext(CustomMockInstructor customMockInstructor) {
        Instructor instructor = instructorRepository.save(EntityCreatorUtil.createInstructor());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(instructor, null, instructor.getAuthorities());
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        SecurityContextHolder.setContext(securityContext);
        TestSecurityContextHolder.setContext(securityContext);
        return securityContext;
    }
}
