package com.example.courseapi.security.config.auditor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;


@Configuration
@EnableJpaAuditing
public class CustomAuditorAwareConfiguration {

    @Bean
    @Primary
    AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(details ->
                    details instanceof User && StringUtils.isNotBlank(((User) details).getUsername()))
                .map(user -> ((User) user).getUsername())
                .or(() -> Optional.of("Anonymous"));
    }
}
