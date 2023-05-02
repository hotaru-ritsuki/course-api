package com.example.courseapi.domain;

import com.example.courseapi.domain.enums.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.DiscriminatorOptions;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

/**
 * Entity class for User
 *
 */
@Entity(name = "User")
@Table(name = "users", schema = "course_management")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("role") // do not use discriminator column since it filters out column when work with entity
@DiscriminatorValue("null")
@DiscriminatorOptions(insert = false, force = true)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity implements UserDetails {
    @Serial
    private static final long serialVersionUID = -6587266125697941080L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "first_name")
    protected String firstName;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "last_name")
    protected String lastName;

    @NotNull
    @Email
    @Column(name = "email")
    protected String email;

    @NotNull
    @Size(min = 8, max = 100)
    @Column(name = "password")
    @JsonIgnore
    protected String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    protected Roles role;

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
