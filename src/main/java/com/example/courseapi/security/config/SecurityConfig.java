package com.example.courseapi.security.config;


import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.security.filters.JwtAuthenticationFilter;
import com.example.courseapi.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.stream.Stream;

/**
 * Security Configuration
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfig {

    @Value("${springdoc.swagger-ui.path:#{null}}")
    private String swaggerUIPath;
    @Value("${springdoc.api-docs.path-all:/v3/api-docs/**}")
    private String swaggerAPIDocsPath;
    @Value("${springdoc.swagger-ui.default-url:/swagger-ui/**}")
    private String swaggerResourceUrl;
    @Value("${spring.debug.requests:false}")
    private Boolean debugRequests;

    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * SecurityFilterChain configuration bean
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider,
            UserDetailsService userDetailsService,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ) throws Exception {
        log.debug("Configuring SecurityFilterChain...");
        http
                .csrf().disable()
                .authorizeHttpRequests()
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userDetailsService, userRepository), UsernamePasswordAuthenticationFilter.class);
        log.debug("SecurityFilterChain successfully created with authentication provider {} and user details service {}",
                authenticationProvider.getClass().getSimpleName(), userDetailsService.getClass().getSimpleName());
        return http.build();
    }

    /**
     * WebSecurityCustomizer configuration bean
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.debug("Configuring WebSecurityCustomizer...");
        return (web) -> {
            String[] filteredRequestMatchers = Stream.of(swaggerUIPath, swaggerAPIDocsPath, swaggerResourceUrl)
                .filter(StringUtils::isNotBlank)
                .toArray(String[]::new);
            web
                .debug(debugRequests)
                .ignoring()
                .requestMatchers(filteredRequestMatchers);
            log.debug("WebSecurityCustomizer successfully created with filtered request matchers: {} and debug flag: {}",
                    filteredRequestMatchers, debugRequests);
        };
    }

    /**
     * AuthenticationProvider configuration bean
     * @return  DaoAuthenticationProvider implementation
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * PasswordEncoder configuration bean
     * @return BCryptPasswordEncoder implementation
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * UserDetails configuration bean
     * @return UserDetails implementation using UserRepository
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email).orElseThrow(() ->
                new SystemException("User with email: " + email + " not found.", ErrorCode.UNAUTHORIZED));
    }
}