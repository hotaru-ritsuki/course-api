package com.example.courseapi.rest;

import com.example.courseapi.config.MockMvcBuilder;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.*;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.UserDTO;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.mapper.UserMapper;
import com.example.courseapi.service.mapper.UserMapper;
import com.example.courseapi.util.TestUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DefaultTestConfiguration
class UserControllerTest {
    private static final String DEFAULT_TITLE = "COURSE_A_TITLE";
    private static final String DEFAULT_DESCRIPTION = "COURSE_A_DESCRIPTION";
    private static final String UPDATED_TITLE = "COURSE_B_TITLE";
    private static final String UPDATED_DESCRIPTION = "COURSE_B_DESCRIPTION";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvcBuilder mockMvcBuilder;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private MockMvc restUserMockMvc;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.restUserMockMvc = mockMvcBuilder.forControllers(userController);
    }

    /**
     * Create an User entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static User createUserEntity() {
        return User.builder()

                .build();
    }

    /**
     * Create an User entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Student createStudentEntity() {
        return Student.builder()
                .firstName("Student FirstName")
                .lastName("Student LastName")
                .email("student@email.com")
                .password("Student TestPassword")
                .role(Roles.STUDENT)
                .build();
    }

    /**
     * Create an User entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Instructor createInstructorEntity() {
        return Instructor.builder()

                .build();
    }

    /**
     * Create an User entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity
     */
    public static Admin createAdminEntity() {
        return Admin.builder()

                .build();
    }

//    @Test
//    @Transactional
//    void createUser() throws Exception {
//        long databaseSizeBeforeCreate = userRepository.count();
//
//        // Create user
//        UserDTO userDTO = userMapper.toDto(createEntity());
//
//        restUserMockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
//                .andExpect(status().isCreated());
//
//        // Validate new User in the database
//        List<User> userList = userRepository.findAll();
//        assertEquals(userList.size(), databaseSizeBeforeCreate + 1);
//
//        User testUser = userList.get(userList.size() - 1);
//        assertThat(testUser.getTitle()).isEqualTo(DEFAULT_TITLE);
//        assertThat(testUser.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
//
//    }
//
//    @Test
//    @Transactional
//    public void createUserWithExistingId() throws Exception {
//        long databaseSizeBeforeCreate = userRepository.count();
//
//        // Create the User with an existing ID
//        UserDTO userDTO = userMapper.toDto(createEntity());
//        userDTO.setId(1L);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restUserMockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
//                .andExpect(status().isBadRequest());
//
//        // Validate the User in the database
//        long databaseSizeAfterCreate = userRepository.count();
//        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkTitleIsRequired() throws Exception {
//        long databaseSizeBeforeCreate = userRepository.count();
//
//        User user = createEntity();
//        // set the field null
//        user.setTitle(null);
//
//        // Create the User, which fails.
//        UserDTO userDTO = userMapper.toDto(user);
//
//        restUserMockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
//                .andExpect(status().isBadRequest());
//
//        // Validate the User in the database
//        long databaseSizeAfterCreate = userRepository.count();
//        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkTitle_shouldHaveLengthFrom2To100() throws Exception {
//        long databaseSizeBeforeCreate = userRepository.count();
//
//        User user = createEntity();
//        // set the field null
//        user.setTitle(RandomStringUtils.randomAlphabetic(1));
//
//        // Create the User, which fails.
//        UserDTO userDTO = userMapper.toDto(user);
//
//        restUserMockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
//                .andExpect(status().isBadRequest());
//
//        // Validate the User in the database
//        long databaseSizeAfterCreate = userRepository.count();
//        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
//
//        user.setTitle(RandomStringUtils.randomAlphabetic(2));
//
//        // Create the User, which fails.
//        userDTO = userMapper.toDto(user);
//
//        restUserMockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
//                .andExpect(status().isCreated());
//
//        // Validate the User in the database
//        databaseSizeAfterCreate = userRepository.count();
//        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);
//
//        databaseSizeBeforeCreate = userRepository.count();
//        user.setTitle(RandomStringUtils.randomAlphabetic(100));
//
//        // Create the User, which fails.
//        userDTO = userMapper.toDto(user);
//
//        restUserMockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
//                .andExpect(status().isCreated());
//
//        // Validate the User in the database
//        databaseSizeAfterCreate = userRepository.count();
//        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);
//    }
//
//    @Test
//    @Transactional
//    public void checkDescriptionIsRequired() throws Exception {
//        long databaseSizeBeforeCreate = userRepository.count();
//
//        User user = createEntity();
//        // set the field null
//        user.setDescription(null);
//
//        // Create the User, which fails.
//        UserDTO userDTO = userMapper.toDto(user);
//
//        restUserMockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
//                .andExpect(status().isBadRequest());
//
//        // Validate the User in the database
//        long databaseSizeAfterCreate = userRepository.count();
//        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkDescription_shouldHaveLengthFrom10To255() throws Exception {
//        long databaseSizeBeforeCreate = userRepository.count();
//
//        User user = createEntity();
//        // set the field null
//        user.setDescription(RandomStringUtils.randomAlphabetic(1));
//
//        // Create the User, which fails.
//        UserDTO userDTO = userMapper.toDto(user);
//
//        restUserMockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
//                .andExpect(status().isBadRequest());
//
//        // Validate the User in the database
//        long databaseSizeAfterCreate = userRepository.count();
//        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
//
//        user.setDescription(RandomStringUtils.randomAlphabetic(10));
//
//        // Create the User, which fails.
//        userDTO = userMapper.toDto(user);
//
//        restUserMockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
//                .andExpect(status().isCreated());
//
//        // Validate the User in the database
//        databaseSizeAfterCreate = userRepository.count();
//        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);
//
//        databaseSizeBeforeCreate = userRepository.count();
//        user.setDescription(RandomStringUtils.randomAlphabetic(255));
//
//        // Create the User, which fails.
//        userDTO = userMapper.toDto(user);
//
//        restUserMockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
//                .andExpect(status().isCreated());
//
//        // Validate the User in the database
//        databaseSizeAfterCreate = userRepository.count();
//        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);
//    }
//
//
//    @Test
//    @Transactional
//    public void getUser() throws Exception {
//        // Initialize the database
//        User user = userRepository.saveAndFlush(createEntity());
//
//        // Get the user
//        restUserMockMvc.perform(get("/api/v1/users/{id}", user.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(user.getId().intValue()))
//                .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
//                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingUser() throws Exception {
//        // Get the user
//        restUserMockMvc.perform(get("/api/v1/users/{id}", Long.MAX_VALUE)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateUser() throws Exception {
//        // Initialize the database
//        User user = userRepository.saveAndFlush(createEntity());
//
//        long databaseSizeBeforeUpdate = userRepository.count();
//
//        // Update the user
//        Optional<User> updatedUserOpt = userRepository.findById(user.getId());
//        assertThat(updatedUserOpt).isPresent();
//        User updatedUser = updatedUserOpt.get();
//        // Disconnect from session so that the updates on updatedUser are not directly saved in db
//        entityManager.detach(updatedUser);
//
//        updatedUser
//                .setTitle(UPDATED_TITLE);
//        updatedUser
//                .setDescription(UPDATED_DESCRIPTION);
//
//        UserDTO userDTO = userMapper.toDto(updatedUser);
//
//        restUserMockMvc.perform(put("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
//                .andExpect(status().isOk());
//
//        // Validate the User in the database
//        List<User> userList = userRepository.findAll();
//        assertThat(userList).hasSize((int) databaseSizeBeforeUpdate);
//        User testUser = userList.get(userList.size() - 1);
//        assertThat(testUser.getTitle()).isEqualTo(UPDATED_TITLE);
//        assertThat(testUser.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingUser() throws Exception {
//        long databaseSizeBeforeUpdate = userRepository.count();
//
//        // Create the User
//        UserDTO userDTO = userMapper.toDto(createEntity());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restUserMockMvc.perform(put("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
//                .andExpect(status().isBadRequest());
//
//        // Validate the User in the database
//        List<User> userList = userRepository.findAll();
//        assertThat(userList).hasSize((int)databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteUser() throws Exception {
//        // Initialize the database
//        User user = userRepository.saveAndFlush(createEntity());
//
//        int databaseSizeBeforeDelete = userRepository.findAll().size();
//
//        // Delete the user
//        restUserMockMvc.perform(delete("/api/v1/users/{id}", user.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        // Validate the database is empty
//        List<User> userList = userRepository.findAll();
//        assertThat(userList).hasSize(databaseSizeBeforeDelete - 1);
//    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(user1.getId());
        assertThat(user1).isEqualTo(user2);
        user2.setId(2L);
        assertThat(user1).isNotEqualTo(user2);
        user1.setId(null);
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDTO.class);
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1L);
        UserDTO userDTO2 = new UserDTO();
        assertThat(userDTO1).isNotEqualTo(userDTO2);
        userDTO2.setId(userDTO1.getId());
        assertThat(userDTO1).isEqualTo(userDTO2);
        userDTO2.setId(2L);
        assertThat(userDTO1).isNotEqualTo(userDTO2);
        userDTO1.setId(null);
        assertThat(userDTO1).isNotEqualTo(userDTO2);
    }
}