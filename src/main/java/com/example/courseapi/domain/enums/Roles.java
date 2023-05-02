package com.example.courseapi.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Enum used for describing user roles
 */
@RequiredArgsConstructor
@Getter
public enum Roles {
    ADMIN("Administrator"),
    INSTRUCTOR("Instructor"),
    STUDENT("Student");

    private final String title;

    /**
     * Converts role enum to a Spring Security friendly authority {@link SimpleGrantedAuthority}
     * @return Spring Security granted authority
     */
    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(String.format("ROLE_%s", name()));
    }
}