package com.example.courseapi.security.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.security.dto.JWTRefreshDTO;
import com.example.courseapi.security.dto.JWTTokenDTO;
import com.example.courseapi.security.dto.LoginRequestDTO;
import com.example.courseapi.security.dto.SignUpRequestDTO;
import com.example.courseapi.security.service.JwtService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthenticationServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AuthenticationServiceImplTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationServiceImpl authenticationServiceImpl;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link AuthenticationServiceImpl#register(SignUpRequestDTO)}
     */
    @Test
    void testRegister() {
        User user = new User();
        user.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("student@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("password");
        user.setRole(Roles.ADMIN);
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(true);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        assertThrows(SystemException.class, () -> authenticationServiceImpl
                .register(new SignUpRequestDTO("FirstName", "LastName", "student@courseapi.org", "password")));
        verify(userRepository).existsByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#register(SignUpRequestDTO)}
     */
    @Test
    void testRegister2() {
        User user = new User();
        user.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("student@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("password");
        user.setRole(Roles.ADMIN);
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");
        authenticationServiceImpl.register(new SignUpRequestDTO("FirstName", "LastName", "student@courseapi.org", "password"));
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).save(Mockito.<User>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
    }


    /**
     * Method under test: {@link AuthenticationServiceImpl#register(SignUpRequestDTO)}
     */
    @Test
    void testRegister4() {
        User user = new User();
        user.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("student@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("password");
        user.setRole(Roles.ADMIN);
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> authenticationServiceImpl
                .register(new SignUpRequestDTO("FirstName", "LastName", "student@courseapi.org", "password")));
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#login(LoginRequestDTO, HttpServletRequest)}
     */
    @Test
    void testLogin() throws AuthenticationException {
        User user = new User();
        user.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("student@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("password");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateJwtAccessToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.generateJwtRefreshToken(Mockito.<User>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("student@courseapi.org", "password");

        JWTTokenDTO actualLoginResult = authenticationServiceImpl.login(loginRequestDTO, new MockHttpServletRequest());
        assertEquals("ABC123", actualLoginResult.getAccessToken());
        assertEquals("ABC123", actualLoginResult.getRefreshToken());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(jwtService).generateJwtAccessToken(Mockito.<UserDetails>any());
        verify(jwtService).generateJwtRefreshToken(Mockito.<User>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#login(LoginRequestDTO, HttpServletRequest)}
     */
    @Test
    void testLogin2() throws AuthenticationException {
        User user = new User();
        user.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("student@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("password");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateJwtAccessToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.generateJwtRefreshToken(Mockito.<User>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenThrow(new SystemException(ErrorCode.OK));
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("student@courseapi.org", "password");

        assertThrows(SystemException.class,
                () -> authenticationServiceImpl.login(loginRequestDTO, new MockHttpServletRequest()));
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#login(LoginRequestDTO, HttpServletRequest)}
     */
    @Test
    void testLogin3() throws AuthenticationException {
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(Optional.empty());
        when(jwtService.generateJwtAccessToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.generateJwtRefreshToken(Mockito.<User>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("student@courseapi.org", "password");

        assertThrows(SystemException.class,
                () -> authenticationServiceImpl.login(loginRequestDTO, new MockHttpServletRequest()));
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#refresh(JWTRefreshDTO, HttpServletRequest)}
     */
    @Test
    void testRefresh() {
        User user = new User();
        user.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("student@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("password");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateJwtAccessToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.generateJwtRefreshToken(Mockito.<User>any())).thenReturn("ABC123");
        when(jwtService.isJwtRefreshTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any())).thenReturn(true);
        when(jwtService.extractUsername(Mockito.<String>any())).thenReturn("janedoe");
        JWTRefreshDTO jwtRefreshDTO = new JWTRefreshDTO("ABC123");
        JWTTokenDTO actualRefreshResult = authenticationServiceImpl.refresh(jwtRefreshDTO, new MockHttpServletRequest());
        assertEquals("ABC123", actualRefreshResult.getAccessToken());
        assertEquals("ABC123", actualRefreshResult.getRefreshToken());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(jwtService).isJwtRefreshTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any());
        verify(jwtService).extractUsername(Mockito.<String>any());
        verify(jwtService).generateJwtAccessToken(Mockito.<UserDetails>any());
        verify(jwtService).generateJwtRefreshToken(Mockito.<User>any());
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#refresh(JWTRefreshDTO, HttpServletRequest)}
     */
    @Test
    void testRefresh2() {
        User user = new User();
        user.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("student@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("password");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateJwtAccessToken(Mockito.<UserDetails>any())).thenThrow(new SystemException(ErrorCode.OK));
        when(jwtService.generateJwtRefreshToken(Mockito.<User>any())).thenThrow(new SystemException(ErrorCode.OK));
        when(jwtService.isJwtRefreshTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any())).thenReturn(true);
        when(jwtService.extractUsername(Mockito.<String>any())).thenReturn("janedoe");
        JWTRefreshDTO jwtRefreshDTO = new JWTRefreshDTO("ABC123");
        assertThrows(SystemException.class,
                () -> authenticationServiceImpl.refresh(jwtRefreshDTO, new MockHttpServletRequest()));
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(jwtService).isJwtRefreshTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any());
        verify(jwtService).extractUsername(Mockito.<String>any());
        verify(jwtService).generateJwtAccessToken(Mockito.<UserDetails>any());
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#refresh(JWTRefreshDTO, HttpServletRequest)}
     */
    @Test
    void testRefresh3() {
        Student student = mock(Student.class);
        Mockito.<Collection<? extends GrantedAuthority>>when(student.getAuthorities()).thenReturn(new ArrayList<>());
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
        student.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("student@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("password");
        student.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(student);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateJwtAccessToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.generateJwtRefreshToken(Mockito.<User>any())).thenReturn("ABC123");
        when(jwtService.isJwtRefreshTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any())).thenReturn(true);
        when(jwtService.extractUsername(Mockito.<String>any())).thenReturn("janedoe");
        JWTRefreshDTO jwtRefreshDTO = new JWTRefreshDTO("ABC123");
        JWTTokenDTO actualRefreshResult = authenticationServiceImpl.refresh(jwtRefreshDTO, new MockHttpServletRequest());
        assertEquals("ABC123", actualRefreshResult.getAccessToken());
        assertEquals("ABC123", actualRefreshResult.getRefreshToken());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(student).getAuthorities();
        verify(student).setCreatedBy(Mockito.<String>any());
        verify(student).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(student).setModifiedBy(Mockito.<String>any());
        verify(student).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(student).setEmail(Mockito.<String>any());
        verify(student).setFirstName(Mockito.<String>any());
        verify(student).setId(Mockito.<Long>any());
        verify(student).setLastName(Mockito.<String>any());
        verify(student).setPassword(Mockito.<String>any());
        verify(student).setRole(Mockito.<Roles>any());
        verify(jwtService).isJwtRefreshTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any());
        verify(jwtService).extractUsername(Mockito.<String>any());
        verify(jwtService).generateJwtAccessToken(Mockito.<UserDetails>any());
        verify(jwtService).generateJwtRefreshToken(Mockito.<User>any());
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#refresh(JWTRefreshDTO, HttpServletRequest)}
     */
    @Test
    void testRefresh4() {
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(Optional.empty());
        Student student = mock(Student.class);
        Mockito.<Collection<? extends GrantedAuthority>>when(student.getAuthorities()).thenReturn(new ArrayList<>());
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
        student.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("student@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("password");
        student.setRole(Roles.ADMIN);
        when(jwtService.generateJwtAccessToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.generateJwtRefreshToken(Mockito.<User>any())).thenReturn("ABC123");
        when(jwtService.isJwtRefreshTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any())).thenReturn(true);
        when(jwtService.extractUsername(Mockito.<String>any())).thenReturn("janedoe");
        JWTRefreshDTO jwtRefreshDTO = new JWTRefreshDTO("ABC123");
        assertThrows(SystemException.class,
                () -> authenticationServiceImpl.refresh(jwtRefreshDTO, new MockHttpServletRequest()));
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(student).setCreatedBy(Mockito.<String>any());
        verify(student).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(student).setModifiedBy(Mockito.<String>any());
        verify(student).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(student).setEmail(Mockito.<String>any());
        verify(student).setFirstName(Mockito.<String>any());
        verify(student).setId(Mockito.<Long>any());
        verify(student).setLastName(Mockito.<String>any());
        verify(student).setPassword(Mockito.<String>any());
        verify(student).setRole(Mockito.<Roles>any());
        verify(jwtService).extractUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#refresh(JWTRefreshDTO, HttpServletRequest)}
     */
    @Test
    void testRefresh5() {
        Student student = mock(Student.class);
        Mockito.<Collection<? extends GrantedAuthority>>when(student.getAuthorities()).thenReturn(new ArrayList<>());
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
        student.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("student@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("password");
        student.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(student);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateJwtAccessToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.generateJwtRefreshToken(Mockito.<User>any())).thenReturn("ABC123");
        when(jwtService.isJwtRefreshTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any())).thenReturn(false);
        when(jwtService.extractUsername(Mockito.<String>any())).thenReturn("janedoe");
        JWTRefreshDTO jwtRefreshDTO = new JWTRefreshDTO("ABC123");
        assertThrows(SystemException.class,
                () -> authenticationServiceImpl.refresh(jwtRefreshDTO, new MockHttpServletRequest()));
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(student).setCreatedBy(Mockito.<String>any());
        verify(student).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(student).setModifiedBy(Mockito.<String>any());
        verify(student).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(student).setEmail(Mockito.<String>any());
        verify(student).setFirstName(Mockito.<String>any());
        verify(student).setId(Mockito.<Long>any());
        verify(student).setLastName(Mockito.<String>any());
        verify(student).setPassword(Mockito.<String>any());
        verify(student).setRole(Mockito.<Roles>any());
        verify(jwtService).isJwtRefreshTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any());
        verify(jwtService).extractUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#refresh(JWTRefreshDTO, HttpServletRequest)}
     */
    @Test
    void testRefresh6() {
        Student student = mock(Student.class);
        Mockito.<Collection<? extends GrantedAuthority>>when(student.getAuthorities()).thenReturn(new ArrayList<>());
        doNothing().when(student).setCreatedBy(Mockito.<String>any());
        doNothing().when(student).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setModifiedBy(Mockito.<String>any());
        doNothing().when(student).setModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(student).setEmail(Mockito.<String>any());
        doNothing().when(student).setFirstName(Mockito.<String>any());
        doNothing().when(student).setId(Mockito.<Long>any());
        doNothing().when(student).setLastName(Mockito.<String>any());
        doNothing().when(student).setPassword(Mockito.<String>any());
        doNothing().when(student).setRole(Mockito.<Roles>any());
        student.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("student@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("password");
        student.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(student);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateJwtAccessToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.generateJwtRefreshToken(Mockito.<User>any())).thenReturn("ABC123");
        when(jwtService.isJwtRefreshTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any())).thenReturn(true);
        when(jwtService.extractUsername(Mockito.<String>any())).thenReturn("");
        JWTRefreshDTO jwtRefreshDTO = new JWTRefreshDTO("ABC123");
        assertThrows(SystemException.class,
                () -> authenticationServiceImpl.refresh(jwtRefreshDTO, new MockHttpServletRequest()));
        verify(student).setCreatedBy(Mockito.<String>any());
        verify(student).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(student).setModifiedBy(Mockito.<String>any());
        verify(student).setModifiedDate(Mockito.<LocalDateTime>any());
        verify(student).setEmail(Mockito.<String>any());
        verify(student).setFirstName(Mockito.<String>any());
        verify(student).setId(Mockito.<Long>any());
        verify(student).setLastName(Mockito.<String>any());
        verify(student).setPassword(Mockito.<String>any());
        verify(student).setRole(Mockito.<Roles>any());
        verify(jwtService).extractUsername(Mockito.<String>any());
    }
}

