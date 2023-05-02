package com.example.courseapi.config.support;

import com.example.courseapi.config.annotation.CustomMockAdmin;
import com.example.courseapi.domain.Admin;
import com.example.courseapi.repository.AdminRepository;
import com.example.courseapi.util.EntityCreatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomMockAdminSecurityContextFactory implements WithSecurityContextFactory<CustomMockAdmin> {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public SecurityContext createSecurityContext(CustomMockAdmin customMockAdmin) {
        Admin admin = adminRepository.save(EntityCreatorUtil.createAdmin());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        SecurityContextHolder.setContext(securityContext);
        TestSecurityContextHolder.setContext(securityContext);
        return securityContext;
    }
}
