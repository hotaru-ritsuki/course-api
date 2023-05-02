package com.example.courseapi.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.response.StudentResponseDTO;
import com.example.courseapi.dto.response.UserResponseDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.mapper.InstructorMapper;
import com.example.courseapi.service.mapper.StudentMapper;
import com.example.courseapi.service.mapper.UserMapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockBean
    private InstructorMapper instructorMapper;

    @MockBean
    private StudentMapper studentMapper;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * Method under test: {@link UserServiceImpl#addRoleToUser(String, Roles)}
     */
    @Test
    void testAddRoleToUser() {
        User user = new User();
        user.setCreatedBy("Anonymous");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("boom.boom@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Anonymous");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("SuperSecuredPassword");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);

        User user2 = new User();
        user2.setCreatedBy("Anonymous");
        user2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setEmail("boom.boom@courseapi.org");
        user2.setFirstName("FirstName");
        user2.setId(1L);
        user2.setLastName("LastName");
        user2.setModifiedBy("Anonymous");
        user2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setPassword("SuperSecuredPassword");
        user2.setRole(Roles.ADMIN);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        userServiceImpl.addRoleToUser("boom.boom@courseapi.org", Roles.ADMIN);
        verify(userRepository).save(Mockito.<User>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addRoleToUser(String, Roles)}
     */
    @Test
    void testAddRoleToUser2() {
        User user = new User();
        user.setCreatedBy("Anonymous");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("boom.boom@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Anonymous");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("SuperSecuredPassword");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.save(Mockito.<User>any())).thenThrow(new SystemException(ErrorCode.OK));
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        assertThrows(SystemException.class, () -> userServiceImpl.addRoleToUser("boom.boom@courseapi.org", Roles.ADMIN));
        verify(userRepository).save(Mockito.<User>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addRoleToUser(String, Roles)}
     */
    @Test
    void testAddRoleToUser3() {
        User user = new User();
        user.setCreatedBy("Anonymous");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("boom.boom@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Anonymous");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("SuperSecuredPassword");
        user.setRole(Roles.ADMIN);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(Optional.empty());
        assertThrows(SystemException.class, () -> userServiceImpl.addRoleToUser("boom.boom@courseapi.org", Roles.ADMIN));
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addRoleToUser(String, Roles)}
     */
    @Test
    void testAddRoleToUser4() {
        User user = new User();
        user.setCreatedBy("Anonymous");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("boom.boom@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Anonymous");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("SuperSecuredPassword");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);

        User user2 = new User();
        user2.setCreatedBy("Anonymous");
        user2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setEmail("boom.boom@courseapi.org");
        user2.setFirstName("FirstName");
        user2.setId(1L);
        user2.setLastName("LastName");
        user2.setModifiedBy("Anonymous");
        user2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setPassword("SuperSecuredPassword");
        user2.setRole(Roles.ADMIN);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        userServiceImpl.addRoleToUser("boom.boom@courseapi.org", Roles.INSTRUCTOR);
        verify(userRepository).save(Mockito.<User>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addRoleToUser(String, Roles)}
     */
    @Test
    void testAddRoleToUser5() {
        User user = new User();
        user.setCreatedBy("Anonymous");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("boom.boom@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Anonymous");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("SuperSecuredPassword");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);

        User user2 = new User();
        user2.setCreatedBy("Anonymous");
        user2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setEmail("boom.boom@courseapi.org");
        user2.setFirstName("FirstName");
        user2.setId(1L);
        user2.setLastName("LastName");
        user2.setModifiedBy("Anonymous");
        user2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setPassword("SuperSecuredPassword");
        user2.setRole(Roles.ADMIN);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        userServiceImpl.addRoleToUser("boom.boom@courseapi.org", Roles.STUDENT);
        verify(userRepository).save(Mockito.<User>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#delete(Long)}
     */
    @Test
    void testDelete() {
        User user = new User();
        user.setCreatedBy("Anonymous");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("boom.boom@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Anonymous");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("SuperSecuredPassword");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        doNothing().when(userRepository).deleteById(Mockito.<Long>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        userServiceImpl.delete(1L);
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#delete(Long)}
     */
    @Test
    void testDelete2() {
        User user = new User();
        user.setCreatedBy("Anonymous");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("boom.boom@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Anonymous");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("SuperSecuredPassword");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        doThrow(new SystemException(ErrorCode.OK)).when(userRepository).deleteById(Mockito.<Long>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(SystemException.class, () -> userServiceImpl.delete(1L));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#delete(Long)}
     */
    @Test
    void testDelete3() {
        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(student);
        doNothing().when(userRepository).delete(Mockito.<User>any());
        doNothing().when(userRepository).deleteById(Mockito.<Long>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        userServiceImpl.delete(1L);
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).delete(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#delete(Long)}
     */
    @Test
    void testDelete4() {
        doNothing().when(userRepository).delete(Mockito.<User>any());
        doNothing().when(userRepository).deleteById(Mockito.<Long>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(SystemException.class, () -> userServiceImpl.delete(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#delete(Long)}
     */
    @Test
    void testDelete5() {
        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(student);
        doThrow(new SystemException(ErrorCode.OK)).when(userRepository).delete(Mockito.<User>any());
        doNothing().when(userRepository).deleteById(Mockito.<Long>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(SystemException.class, () -> userServiceImpl.delete(1L));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).delete(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#assingRoleForUser(Long, Roles)}
     */
    @Test
    void testAssingRoleForUser() {
        User user = new User();
        user.setCreatedBy("Anonymous");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("boom.boom@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Anonymous");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("SuperSecuredPassword");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);

        User user2 = new User();
        user2.setCreatedBy("Anonymous");
        user2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setEmail("boom.boom@courseapi.org");
        user2.setFirstName("FirstName");
        user2.setId(1L);
        user2.setLastName("LastName");
        user2.setModifiedBy("Anonymous");
        user2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setPassword("SuperSecuredPassword");
        user2.setRole(Roles.ADMIN);
        when(userRepository.saveAndFlush(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setCreatedBy("Anonymous");
        userResponseDTO.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResponseDTO.setEmail("boom.boom@courseapi.org");
        userResponseDTO.setFirstName("FirstName");
        userResponseDTO.setId(1L);
        userResponseDTO.setLastName("LastName");
        userResponseDTO.setModifiedBy("Anonymous");
        userResponseDTO.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(userMapper.toResponseDto(Mockito.<User>any())).thenReturn(userResponseDTO);
        assertSame(userResponseDTO, userServiceImpl.assingRoleForUser(1L, Roles.ADMIN));
        verify(userRepository).saveAndFlush(Mockito.<User>any());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userMapper).toResponseDto(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#assingRoleForUser(Long, Roles)}
     */
    @Test
    void testAssingRoleForUser2() {
        User user = new User();
        user.setCreatedBy("Anonymous");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("boom.boom@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Anonymous");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("SuperSecuredPassword");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);

        User user2 = new User();
        user2.setCreatedBy("Anonymous");
        user2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setEmail("boom.boom@courseapi.org");
        user2.setFirstName("FirstName");
        user2.setId(1L);
        user2.setLastName("LastName");
        user2.setModifiedBy("Anonymous");
        user2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setPassword("SuperSecuredPassword");
        user2.setRole(Roles.ADMIN);
        when(userRepository.saveAndFlush(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(userMapper.toResponseDto(Mockito.<User>any())).thenThrow(new SystemException(ErrorCode.OK));
        assertThrows(SystemException.class, () -> userServiceImpl.assingRoleForUser(1L, Roles.ADMIN));
        verify(userRepository).saveAndFlush(Mockito.<User>any());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userMapper).toResponseDto(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#assingRoleForUser(Long, Roles)}
     */
    @Test
    void testAssingRoleForUser3() {
        User user = new User();
        user.setCreatedBy("Anonymous");
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("boom.boom@courseapi.org");
        user.setFirstName("FirstName");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("Anonymous");
        user.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setPassword("SuperSecuredPassword");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);

        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        when(userRepository.saveAndFlush(Mockito.<User>any())).thenReturn(student);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setCreatedBy("Anonymous");
        userResponseDTO.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResponseDTO.setEmail("boom.boom@courseapi.org");
        userResponseDTO.setFirstName("FirstName");
        userResponseDTO.setId(1L);
        userResponseDTO.setLastName("LastName");
        userResponseDTO.setModifiedBy("Anonymous");
        userResponseDTO.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(userMapper.toResponseDto(Mockito.<User>any())).thenReturn(userResponseDTO);
        StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
        when(studentMapper.toResponseDto(Mockito.<Student>any())).thenReturn(studentResponseDTO);
        assertSame(studentResponseDTO, userServiceImpl.assingRoleForUser(1L, Roles.ADMIN));
        verify(userRepository).saveAndFlush(Mockito.<User>any());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(studentMapper).toResponseDto(Mockito.<Student>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#assingRoleForUser(Long, Roles)}
     */
    @Test
    void testAssingRoleForUser4() {
        Student student = new Student();
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        student.setStudentCourses(new HashSet<>());
        student.setCreatedBy("Anonymous");
        student.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setEmail("boom.boom@courseapi.org");
        student.setFirstName("FirstName");
        student.setId(1L);
        student.setLastName("LastName");
        student.setModifiedBy("Anonymous");
        student.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        student.setPassword("SuperSecuredPassword");
        student.setRole(Roles.ADMIN);
        when(userRepository.saveAndFlush(Mockito.<User>any())).thenReturn(student);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setCreatedBy("Anonymous");
        userResponseDTO.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResponseDTO.setEmail("boom.boom@courseapi.org");
        userResponseDTO.setFirstName("FirstName");
        userResponseDTO.setId(1L);
        userResponseDTO.setLastName("LastName");
        userResponseDTO.setModifiedBy("Anonymous");
        userResponseDTO.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(userMapper.toResponseDto(Mockito.<User>any())).thenReturn(userResponseDTO);
        when(studentMapper.toResponseDto(Mockito.<Student>any())).thenReturn(new StudentResponseDTO());
        assertThrows(SystemException.class, () -> userServiceImpl.assingRoleForUser(1L, Roles.ADMIN));
        verify(userRepository).findById(Mockito.<Long>any());
    }
}

