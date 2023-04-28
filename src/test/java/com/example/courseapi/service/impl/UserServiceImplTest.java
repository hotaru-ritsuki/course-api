package com.example.courseapi.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.StudentResponseDTO;
import com.example.courseapi.dto.UserRequestDTO;
import com.example.courseapi.dto.UserResponseDTO;
import com.example.courseapi.exception.UserNotFoundException;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.mapper.InstructorMapper;
import com.example.courseapi.service.mapper.StudentMapper;
import com.example.courseapi.service.mapper.UserMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.example.courseapi.service.mapper.UserRequestMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private UserRequestMapper userRequestMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Method under test: {@link UserServiceImpl#getUsers(Filters, Pageable)}
     */
    @Test
    void testGetUsers() {
        Page<User> userPage = new PageImpl<>(new ArrayList<>());
        Page<UserResponseDTO> userDTOPage = new PageImpl<>(new ArrayList<>());
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        when(userRepository.findAll(Mockito.<Specification<User>>any(), Mockito.<Pageable>any())).thenReturn(userPage);
        when(userMapper.toDto(Mockito.<User>any())).thenReturn(userResponseDTO);
        Page<UserResponseDTO> actualUsers = userServiceImpl.getUsers(Mockito.<Filters>any(), Mockito.<Pageable>any());
        assertTrue(actualUsers.isEmpty());
        assertTrue(userDTOPage.isEmpty());
        verify(userRepository).findAll(Mockito.<Specification<User>>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUsers(Filters, Pageable)}
     */
    @Test
    void testGetUsers2() {
        when(userRepository.findAll(Mockito.<Specification<User>>any(), Mockito.<Pageable>any())).thenThrow(new UserNotFoundException());
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUsers(Mockito.<Filters>any(), Mockito.<Pageable>any()));
        verify(userRepository).findAll(Mockito.<Specification<User>>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#saveUser(com.example.courseapi.dto.UserRequestDTO)}
     */
    @Test
    void testSaveUser() {
        User user = new User();
        user.setCreatedBy("username");
        user.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setEmail("user@courseapi.com");
        user.setFirstName("User");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("username");
        user.setPassword("iloveyou");
        user.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setRole(Roles.ADMIN);

        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setEmail("user@courseapi.com");
        userDTO.setFirstName("User");
        userDTO.setId(1L);
        userDTO.setLastName("LastName");

        UserRequestDTO user2 = new UserRequestDTO();
        user2.setEmail("user@courseapi.com");
        user2.setFirstName("User");
        user2.setId(1L);
        user2.setLastName("LastName");

        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        when(userRequestMapper.toDto(Mockito.<User>any())).thenReturn(user2);
        when(userMapper.toDto(Mockito.<User>any())).thenReturn(userDTO);

        assertSame(userDTO, userServiceImpl.saveUser(user2));
        verify(userRepository).save(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#saveUser(UserRequestDTO)}
     */
    @Test
    void testSaveUser2() {
        when(userRepository.save(Mockito.<User>any())).thenThrow(new UserNotFoundException());

        UserRequestDTO userDTO = new UserRequestDTO();
        userDTO.setEmail("user@courseapi.com");
        userDTO.setFirstName("User");
        userDTO.setId(1L);
        userDTO.setLastName("LastName");
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.saveUser(userDTO));
        verify(userRepository).save(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addRoleToUser(String, Roles)}
     */
    @Test
    void testAddRoleToUser() {
        User user = new User();
        user.setCreatedBy("username");
        user.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setEmail("user@courseapi.com");
        user.setFirstName("User");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("username");
        user.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setPassword("iloveyou");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);

        User user2 = new User();
        user2.setCreatedBy("username");
        user2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user2.setEmail("user@courseapi.com");
        user2.setFirstName("User");
        user2.setId(1L);
        user2.setLastName("LastName");
        user2.setModifiedBy("username");
        user2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user2.setPassword("iloveyou");
        user2.setRole(Roles.ADMIN);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        userServiceImpl.addRoleToUser("user@courseapi.com", Roles.ADMIN);
        verify(userRepository).save(Mockito.<User>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addRoleToUser(String, Roles)}
     */
    @Test
    void testAddRoleToUser2() {
        User user = new User();
        user.setCreatedBy("username");
        user.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setEmail("user@courseapi.com");
        user.setFirstName("User");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("username");
        user.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setPassword("iloveyou");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.save(Mockito.<User>any())).thenThrow(new UserNotFoundException());
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.addRoleToUser("user@courseapi.com", Roles.ADMIN));
        verify(userRepository).save(Mockito.<User>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addRoleToUser(String, Roles)}
     */
    @Test
    void testAddRoleToUser3() {
        User user = new User();
        user.setCreatedBy("username");
        user.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setEmail("user@courseapi.com");
        user.setFirstName("User");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("username");
        user.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setPassword("iloveyou");
        user.setRole(Roles.ADMIN);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.addRoleToUser("user@courseapi.com", Roles.ADMIN));
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addRoleToUser(String, Roles)}
     */
    @Test
    void testAddRoleToUser4() {
        User user = new User();
        user.setCreatedBy("username");
        user.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setEmail("user@courseapi.com");
        user.setFirstName("User");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("username");
        user.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setPassword("iloveyou");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);

        User user2 = new User();
        user2.setCreatedBy("username");
        user2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user2.setEmail("user@courseapi.com");
        user2.setFirstName("User");
        user2.setId(1L);
        user2.setLastName("LastName");
        user2.setModifiedBy("username");
        user2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user2.setPassword("iloveyou");
        user2.setRole(Roles.ADMIN);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        userServiceImpl.addRoleToUser("user@courseapi.com", Roles.INSTRUCTOR);
        verify(userRepository).save(Mockito.<User>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addRoleToUser(String, Roles)}
     */
    @Test
    void testAddRoleToUser5() {
        User user = new User();
        user.setCreatedBy("username");
        user.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setEmail("user@courseapi.com");
        user.setFirstName("User");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("username");
        user.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setPassword("iloveyou");
        user.setRole(Roles.ADMIN);
        Optional<User> ofResult = Optional.of(user);

        User user2 = new User();
        user2.setCreatedBy("username");
        user2.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user2.setEmail("user@courseapi.com");
        user2.setFirstName("User");
        user2.setId(1L);
        user2.setLastName("LastName");
        user2.setModifiedBy("username");
        user2.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user2.setPassword("iloveyou");
        user2.setRole(Roles.ADMIN);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        userServiceImpl.addRoleToUser("user@courseapi.com", Roles.STUDENT);
        verify(userRepository).save(Mockito.<User>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findUserByEmail(String)}
     */
    @Test
    void testFindUserByEmail() {
        User user = new User();
        user.setCreatedBy("username");
        user.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setEmail("user@courseapi.com");
        user.setFirstName("User");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("username");
        user.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setPassword("iloveyou");
        user.setRole(Roles.ADMIN);
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setCreatedBy("username");
        userDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        userDTO.setEmail("user@courseapi.com");
        userDTO.setFirstName("User");
        userDTO.setId(1L);
        userDTO.setLastName("LastName");
        userDTO.setModifiedBy("username");
        userDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(userMapper.toDto(Mockito.<User>any())).thenReturn(userDTO);
        assertSame(userDTO, userServiceImpl.findUserByEmail("user@courseapi.com"));
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findUserByEmail(String)}
     */
    @Test
    void testFindUserByEmail3() {
        when(userRepository.findByEmail(Mockito.<String>any())).thenThrow(new UserNotFoundException());
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.findUserByEmail("user@courseapi.com"));
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserById(Long)}
     */
    @Test
    void testGetUserById() {
        User user = new User();
        user.setCreatedBy("username");
        user.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setEmail("user@courseapi.com");
        user.setFirstName("User");
        user.setId(1L);
        user.setLastName("LastName");
        user.setModifiedBy("username");
        user.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        user.setPassword("iloveyou");
        user.setRole(Roles.ADMIN);
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setCreatedBy("username");
        userDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        userDTO.setEmail("user@courseapi.com");
        userDTO.setFirstName("User");
        userDTO.setId(1L);
        userDTO.setLastName("LastName");
        userDTO.setModifiedBy("username");
        userDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(userMapper.toDto(Mockito.<User>any())).thenReturn(userDTO);
        assertSame(userDTO, userServiceImpl.getUserById(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserById(Long)}
     */
    @Test
    void testGetUserById3() {
        when(userRepository.findById(Mockito.<Long>any())).thenThrow(new UserNotFoundException());
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUserById(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#mapCurrentUser(User)}
     */
    @Test
    void testMapCurrentUser() {
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setCreatedBy("username");
        userDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        userDTO.setEmail("user@courseapi.com");
        userDTO.setFirstName("User");
        userDTO.setId(1L);
        userDTO.setLastName("LastName");
        userDTO.setModifiedBy("username");
        userDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        when(userMapper.toDto(Mockito.<User>any())).thenReturn(userDTO);

        User currentUser = new User();
        currentUser.setCreatedBy("username");
        currentUser.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        currentUser.setEmail("user@courseapi.com");
        currentUser.setFirstName("User");
        currentUser.setId(1L);
        currentUser.setLastName("LastName");
        currentUser.setModifiedBy("username");
        currentUser.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        currentUser.setPassword("iloveyou");
        currentUser.setRole(Roles.ADMIN);
        assertSame(userDTO, userServiceImpl.mapCurrentUser(currentUser));
        verify(userMapper).toDto(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#mapCurrentUser(User)}
     */
    @Test
    void testMapCurrentUser2() {
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setCreatedBy("username");
        userDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        userDTO.setEmail("user@courseapi.com");
        userDTO.setFirstName("User");
        userDTO.setId(1L);
        userDTO.setLastName("LastName");
        userDTO.setModifiedBy("username");
        userDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        when(userMapper.toDto(Mockito.<User>any())).thenReturn(userDTO);

        StudentResponseDTO studentDTO = new StudentResponseDTO();
        studentDTO.setCreatedBy("username");
        studentDTO.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        studentDTO.setEmail("user@courseapi.com");
        studentDTO.setFirstName("User");
        studentDTO.setId(1L);
        studentDTO.setLastName("LastName");
        studentDTO.setModifiedBy("username");
        studentDTO.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        when(studentMapper.toDto(Mockito.<Student>any())).thenReturn(studentDTO);

        Student currentUser = new Student();
//        currentUser.setCourseFeedbacks(new HashSet<>());
        currentUser.setCreatedBy("username");
        currentUser.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        currentUser.setEmail("user@courseapi.com");
        currentUser.setFirstName("User");
        currentUser.setId(1L);
        currentUser.setLastName("LastName");
        currentUser.setModifiedBy("username");
        currentUser.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        currentUser.setPassword("iloveyou");
        currentUser.setRole(Roles.ADMIN);
        currentUser.setStudentCourses(new HashSet<>());
        currentUser.setCreatedBy("username");
        currentUser.setCreatedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        currentUser.setEmail("user@courseapi.com");
        currentUser.setFirstName("User");
        currentUser.setId(1L);
        currentUser.setLastName("LastName");
        currentUser.setModifiedBy("username");
        currentUser.setModifiedDate(LocalDate.of(2023, 4, 20).atStartOfDay());
        currentUser.setPassword("iloveyou");
        currentUser.setRole(Roles.ADMIN);
        assertSame(studentDTO, userServiceImpl.mapCurrentUser(currentUser));
        verify(studentMapper).toDto(Mockito.<Student>any());
    }
}

