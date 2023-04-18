package com.example.courseapi.domain;

import com.example.courseapi.domain.enums.Roles;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Entity class for User
 *
 */
@Entity
@Table(name = "users", schema = "course_management")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Email
    @Column(name = "email")
    private String email;

    @NotNull
    @Size(min = 8, max = 100)
    @Column(name = "password")
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Roles role;

    @ManyToMany(mappedBy = "students")
    private Set<Course> studentCourses;

    @ManyToMany(mappedBy = "instructors")
    private Set<Course> instructorCourses;

    /**
     * Get user authorities to manage role based access.
     * Implementation of method of Spring {@link UserDetails} class.
     * @return collection containing granted authorities {@link GrantedAuthority}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(this.role.getAuthority());
    }

    /**
     * Provides email as a username
     * Implementation of method of Spring {@link UserDetails} class.
     * @return email of the user
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
