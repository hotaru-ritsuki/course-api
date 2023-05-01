package com.example.courseapi.rest;

import com.example.courseapi.config.MockMvcBuilderTestConfiguration;
import com.example.courseapi.config.annotation.CustomMockAdmin;
import com.example.courseapi.config.annotation.CustomMockInstructor;
import com.example.courseapi.config.annotation.CustomMockStudent;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.*;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.request.RoleRequestDTO;
import com.example.courseapi.dto.request.UserRequestDTO;
import com.example.courseapi.dto.response.UserResponseDTO;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.mapper.UserMapper;
import com.example.courseapi.util.EntityCreatorUtil;
import com.example.courseapi.util.TestUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DefaultTestConfiguration
class UserControllerTest {
    private static final String UPDATED_FIRSTNAME = "A_FIRSTNAME";
    private static final String UPDATED_LASTNAME = "A_LASTNAME";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvcBuilderTestConfiguration mockMvcBuilderTestConfiguration;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private MockMvc restUserMockMvc;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
        this.restUserMockMvc = mockMvcBuilderTestConfiguration.forControllers(userController);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    void createUser() throws Exception {
        long databaseSizeBeforeCreate = userRepository.count();

        // Create user
        UserRequestDTO userDTO = userMapper.toRequestDto(EntityCreatorUtil.createUser(""));

        restUserMockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
                .andExpect(status().isCreated());

        // Validate new User in the database
        long databaseSizeAfterCreate= userRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        Optional<User> testUserOpt = userRepository.findByEmail(userDTO.getEmail());
        assertThat(testUserOpt).isPresent();
        User testUser = testUserOpt.get();
        assertThat(testUser.getId()).isNotNull();
        assertThat(testUser.getFirstName()).isEqualTo("FirstName#");
        assertThat(testUser.getLastName()).isEqualTo("LastName#");
        assertThat(testUser.getRole()).isEqualTo(Roles.STUDENT);
        assertThat(testUser.getEmail()).isEqualTo("user@email.com");
        assertThat(testUser.getPassword()).isNotNull();
    }

    @Test
    @Transactional
    @CustomMockAdmin
    void assignInstructorRoleToUser() throws Exception {
        // Create user
        User savedUser = EntityCreatorUtil.createUser("");
        savedUser = userRepository.saveAndFlush(savedUser);

        RoleRequestDTO roleRequestDTO = new RoleRequestDTO(Roles.INSTRUCTOR);

        restUserMockMvc.perform(post("/api/v1/users/" + savedUser.getId() + "/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(roleRequestDTO)))
                .andExpect(status().isOk());

        // Validate new User in the database
        Optional<User> testUserOpt = userRepository.findByEmail(savedUser.getEmail());
        assertThat(testUserOpt).isPresent();
        User testUser = testUserOpt.get();
        assertThat(testUser.getId()).isNotNull();
        assertThat(testUser.getFirstName()).isEqualTo("FirstName#");
        assertThat(testUser.getLastName()).isEqualTo("LastName#");
        assertThat(testUser.getEmail()).isEqualTo("user@email.com");
        assertThat(testUser.getPassword()).isNotNull();
        assertThat(testUser.getRole()).isEqualTo(Roles.INSTRUCTOR);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    void assignAdminRoleToUser() throws Exception {
        // Create user
        User savedUser = EntityCreatorUtil.createUser("");
        savedUser = userRepository.saveAndFlush(savedUser);

        RoleRequestDTO roleRequestDTO = new RoleRequestDTO(Roles.ADMIN);

        restUserMockMvc.perform(post("/api/v1/users/" + savedUser.getId() + "/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(roleRequestDTO)))
                .andExpect(status().isOk());

        // Validate new User in the database
        Optional<User> testUserOpt = userRepository.findByEmail(savedUser.getEmail());
        assertThat(testUserOpt).isPresent();
        User testUser = testUserOpt.get();
        assertThat(testUser.getId()).isNotNull();
        assertThat(testUser.getFirstName()).isEqualTo("FirstName#");
        assertThat(testUser.getLastName()).isEqualTo("LastName#");
        assertThat(testUser.getEmail()).isEqualTo("user@email.com");
        assertThat(testUser.getPassword()).isNotNull();
        assertThat(testUser.getRole()).isEqualTo(Roles.ADMIN);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void createUserWithExistingId() throws Exception {
        long databaseSizeBeforeCreate = userRepository.count();

        // Create the User with an existing ID
        UserRequestDTO userDTO = userMapper.toRequestDto(EntityCreatorUtil.createUser(""));
        userDTO.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
                .andExpect(status().isBadRequest());

        // Validate the User in the database
        long databaseSizeAfterCreate = userRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeCreate = userRepository.count();

        User user = EntityCreatorUtil.createUser("1");;
        // set the field null
        user.setFirstName(null);

        // Create the User, which fails.
        UserRequestDTO userDTO = userMapper.toRequestDto(user);

        restUserMockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
                .andExpect(status().isBadRequest());

        // Validate the User in the database
        long databaseSizeAfterCreate = userRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void checkFirstName_shouldHaveLengthFrom2To50() throws Exception {
        long databaseSizeBeforeCreate = userRepository.count();

        User user = EntityCreatorUtil.createUser("1");
        // set the field null
        user.setFirstName(RandomStringUtils.randomAlphabetic(1));

        // Create the User, which fails.
        UserRequestDTO userDTO = userMapper.toRequestDto(user);

        restUserMockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
                .andExpect(status().isBadRequest());

        // Validate the User in the database
        long databaseSizeAfterCreate = userRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate);

        user.setFirstName(RandomStringUtils.randomAlphabetic(2));

        // Create the User, which fails.
        userDTO = userMapper.toRequestDto(user);

        restUserMockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
                .andExpect(status().isCreated());

        // Validate the User in the database
        databaseSizeAfterCreate = userRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        databaseSizeBeforeCreate = userRepository.count();
        user.setFirstName(RandomStringUtils.randomAlphabetic(50));

        // Create the User, which fails.
        userDTO = userMapper.toRequestDto(user);

        restUserMockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
                .andExpect(status().isCreated());

        // Validate the User in the database
        databaseSizeAfterCreate = userRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void getUser() throws Exception {
        // Initialize the database
        User user = userRepository.saveAndFlush(EntityCreatorUtil.createUser(""));

        // Get the user
        restUserMockMvc.perform(get("/api/v1/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId().intValue()))
                .andExpect(jsonPath("$.firstName").value("FirstName#"))
                .andExpect(jsonPath("$.lastName").value("LastName#"))
                .andExpect(jsonPath("$.email").value("user@email.com"));
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void getNonExistingUser() throws Exception {
        // Get the user
        restUserMockMvc.perform(get("/api/v1/users/{id}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void updateUser() throws Exception {
        // Initialize the database
        User user = userRepository.saveAndFlush(EntityCreatorUtil.createUser(""));

        long databaseSizeBeforeUpdate = userRepository.count();

        // Update the user
        Optional<User> updatedUserOpt = userRepository.findById(user.getId());
        assertThat(updatedUserOpt).isPresent();
        User updatedUser = updatedUserOpt.get();
        // Disconnect from session so that the updates on updatedUser are not directly saved in db
        entityManager.detach(updatedUser);

        updatedUser
                .setFirstName(UPDATED_FIRSTNAME);
        updatedUser
                .setLastName(UPDATED_LASTNAME);

        UserRequestDTO userDTO = userMapper.toRequestDto(updatedUser);

        restUserMockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        // Validate the User in the database
        Long databaseSizeAfterUpdate = userRepository.count();
        assertThat(databaseSizeAfterUpdate).isEqualTo(databaseSizeBeforeUpdate);
        Optional<User> checkUserOpt = userRepository.findById(user.getId());
        assertThat(checkUserOpt).isPresent();
        User checkUser = checkUserOpt.get();
        assertThat(checkUser.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(checkUser.getLastName()).isEqualTo(UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void updateNonExistingUser() throws Exception {
        long databaseSizeBeforeUpdate = userRepository.count();

        // Create the User
        UserRequestDTO userDTO = userMapper.toRequestDto(EntityCreatorUtil.createUser(""));

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDTO)))
                .andExpect(status().isBadRequest());

        // Validate the User in the database
        long databaseSizeAfterUpdate = userRepository.count();
        assertThat(databaseSizeAfterUpdate).isEqualTo(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void deleteUser() throws Exception {
        // Initialize the database
        User user = userRepository.saveAndFlush(EntityCreatorUtil.createUser(""));

        long databaseSizeBeforeDelete = userRepository.count();

        // Delete the user
        restUserMockMvc.perform(delete("/api/v1/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database is empty
        long databaseSizeAfterDelete = userRepository.count();

        assertThat(databaseSizeAfterDelete).isEqualTo(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void getAllUsers() throws Exception {
        // Initialize the database
        User user = userRepository.saveAndFlush(EntityCreatorUtil.createUser(""));

        // Get all the userList
        restUserMockMvc.perform(
                        get("/api/v1/users?sort=id,desc&filter=id:equals:" + user.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)));
    }

    @Test
    @Transactional
    @CustomMockStudent
    public void getMeAsStudent() throws Exception {
        restUserMockMvc.perform(
                        get("/api/v1/users/me")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", Matchers.is("student@email.com")))
                .andExpect(jsonPath("$.firstName", Matchers.is("FirstName#")))
                .andExpect(jsonPath("$.lastName", Matchers.is("LastName#")))
                .andExpect(jsonPath("$.studentCourseIds").exists())
                .andExpect(jsonPath("$.studentCourseIds").isArray())
                .andExpect(jsonPath("$.instructorCourseIds").doesNotExist());
    }

    @Test
    @Transactional
    @CustomMockAdmin
    public void getMeAsAdmin() throws Exception {
        restUserMockMvc.perform(
                        get("/api/v1/users/me")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", Matchers.is("admin@email.com")))
                .andExpect(jsonPath("$.firstName", Matchers.is("FirstName#")))
                .andExpect(jsonPath("$.lastName", Matchers.is("LastName#")))
                .andExpect(jsonPath("$.studentCourseIds").doesNotExist())
                .andExpect(jsonPath("$.instructorCourseIds").doesNotExist());
    }

    @Test
    @Transactional
    @CustomMockInstructor
    public void getMeAsInstructor() throws Exception {
        restUserMockMvc.perform(
                        get("/api/v1/users/me")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", Matchers.is("instructor@email.com")))
                .andExpect(jsonPath("$.firstName", Matchers.is("FirstName#")))
                .andExpect(jsonPath("$.lastName", Matchers.is("LastName#")))
                .andExpect(jsonPath("$.studentCourseIds").doesNotExist())
                .andExpect(jsonPath("$.instructorCourseIds").exists())
                .andExpect(jsonPath("$.instructorCourseIds").isArray());
    }

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
    public void responseDtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserResponseDTO.class);
        UserResponseDTO userDTO1 = new UserResponseDTO();
        userDTO1.setId(1L);
        UserResponseDTO userDTO2 = new UserResponseDTO();
        assertThat(userDTO1).isNotEqualTo(userDTO2);
        userDTO2.setId(userDTO1.getId());
        assertThat(userDTO1).isEqualTo(userDTO2);
        userDTO2.setId(2L);
        assertThat(userDTO1).isNotEqualTo(userDTO2);
        userDTO1.setId(null);
        assertThat(userDTO1).isNotEqualTo(userDTO2);
    }

    @Test
    @Transactional
    public void requestDtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRequestDTO.class);
        UserRequestDTO userDTO1 = new UserRequestDTO();
        userDTO1.setId(1L);
        UserRequestDTO userDTO2 = new UserRequestDTO();
        assertThat(userDTO1).isNotEqualTo(userDTO2);
        userDTO2.setId(userDTO1.getId());
        assertThat(userDTO1).isEqualTo(userDTO2);
        userDTO2.setId(2L);
        assertThat(userDTO1).isNotEqualTo(userDTO2);
        userDTO1.setId(null);
        assertThat(userDTO1).isNotEqualTo(userDTO2);
    }
}