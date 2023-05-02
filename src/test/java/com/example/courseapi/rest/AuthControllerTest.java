package com.example.courseapi.rest;

import com.example.courseapi.config.MockMvcBuilderTestConfiguration;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.response.UserResponseDTO;
import com.example.courseapi.dto.request.UserRequestDTO;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.security.controller.AuthController;
import com.example.courseapi.security.dto.JWTRefreshDTO;
import com.example.courseapi.security.dto.JWTTokenDTO;
import com.example.courseapi.security.dto.LoginRequestDTO;
import com.example.courseapi.security.dto.SignUpRequestDTO;
import com.example.courseapi.security.service.JwtService;
import com.example.courseapi.util.EntityCreatorUtil;
import com.example.courseapi.util.JacksonUtil;
import com.example.courseapi.util.TestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DefaultTestConfiguration
class AuthControllerTest {
    private static final String UPDATED_FIRSTNAME = "A_FIRSTNAME";
    private static final String UPDATED_LASTNAME = "A_LASTNAME";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvcBuilderTestConfiguration mockMvcBuilderTestConfiguration;

    @Autowired
    private AuthController authController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private MockMvc restUserMockMvc;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
        this.restUserMockMvc = mockMvcBuilderTestConfiguration.forControllers(authController);
    }

    @Test
    @Transactional
    void registerUser() throws Exception {
        long databaseSizeBeforeCreate = userRepository.count();

        User user = EntityCreatorUtil.createUser("");

        // Create user
        SignUpRequestDTO signUpRequestDTO = SignUpRequestDTO.builder()
                .email(user.getEmail())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .password(user.getPassword())
                .build();

        restUserMockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(signUpRequestDTO)))
                .andExpect(status().isOk());


        // Validate new User in the database
        long databaseSizeAfterCreate= userRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        Optional<User> testUserOpt = userRepository.findByEmail(signUpRequestDTO.getEmail());
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
    void loginUser() throws Exception {
        long databaseSizeBeforeCreate = userRepository.count();

        User user = userRepository.saveAndFlush(EntityCreatorUtil.createUser(""));

        // Create user
        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .email(user.getEmail())
                .password("password")
                .build();

        MvcResult mvcResult = restUserMockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(loginRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();

        JWTTokenDTO jwtTokenDTO = JacksonUtil.deserialize(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertTrue(jwtService.isJwtTokenValid(jwtTokenDTO.getAccessToken(), user));
        assertTrue(jwtService.isJwtTokenValid(jwtTokenDTO.getRefreshToken(), user));

        // Validate new User in the database
        long databaseSizeAfterCreate= userRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        Optional<User> testUserOpt = userRepository.findByEmail(loginRequestDTO.getEmail());
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
    void loginUserWithInvalidPassword() throws Exception {

        User user = userRepository.saveAndFlush(EntityCreatorUtil.createUser(""));

        // Create user
        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .email(user.getEmail())
                .password("passwordinnioincorrect")
                .build();

        restUserMockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(loginRequestDTO)))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @Transactional
    void refreshAccessToken() throws Exception {
        long databaseSizeBeforeCreate = userRepository.count();

        User user = userRepository.saveAndFlush(EntityCreatorUtil.createUser(""));

        String jwtRefreshToken = jwtService.generateJwtToken(user, true);
        // Create user
        JWTRefreshDTO jwtRefreshDTO = JWTRefreshDTO.builder()
                .refreshToken(jwtRefreshToken)
                .build();

        MvcResult mvcResult = restUserMockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(jwtRefreshDTO)))
                .andExpect(status().isOk())
                .andReturn();

        JWTTokenDTO jwtTokenDTO = JacksonUtil.deserialize(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertTrue(jwtService.isJwtTokenValid(jwtTokenDTO.getAccessToken(), user));
        assertTrue(jwtService.isJwtTokenValid(jwtTokenDTO.getRefreshToken(), user));

        // Validate new User in the database
        long databaseSizeAfterCreate= userRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        Optional<User> testUserOpt = userRepository.findByEmail(user.getEmail());
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