package com.example.courseapi.config.support;

import com.example.courseapi.config.annotation.CustomMockStudent;
import com.example.courseapi.domain.Student;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.util.EntityCreatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomMockStudentSecurityContextFactory implements WithSecurityContextFactory<CustomMockStudent> {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public SecurityContext createSecurityContext(CustomMockStudent customMockStudent) {
        Student student = studentRepository.save(EntityCreatorUtil.createStudent());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(student, null, student.getAuthorities());
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        SecurityContextHolder.setContext(securityContext);
        TestSecurityContextHolder.setContext(securityContext);
        return securityContext;
    }
}
