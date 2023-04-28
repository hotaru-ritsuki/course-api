package com.example.courseapi.e2e;

import com.example.courseapi.config.MockMvcBuilder;
import com.example.courseapi.config.annotation.DefaultTestConfiguration;
import com.example.courseapi.domain.User;
import com.example.courseapi.dto.UserResponseDTO;
import com.example.courseapi.security.dto.JWTRefreshDTO;
import com.example.courseapi.security.dto.JWTTokenDTO;
import com.example.courseapi.security.dto.LoginRequestDTO;
import com.example.courseapi.security.dto.SignUpRequestDTO;
import com.example.courseapi.util.EntityCreatorUtil;
import com.example.courseapi.util.JacksonUtil;
import com.example.courseapi.util.TestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Scenarios:
 * 1. Registration, Authentication and Role Management
 * 2. Course Management
 * 3. Homework and Grading
 */
@DefaultTestConfiguration
class CourseManagementScenariosTest {

    @Autowired
    private MockMvcBuilder mockMvcBuilder;

    private MockMvc restUserMockMvc;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
        this.restUserMockMvc = mockMvcBuilder.fromSecurityWebAppContext();
    }

    @Test
    @Transactional
    void scenario1_userRegistrationAndLogin() throws Exception {
        // Register user
        User user = EntityCreatorUtil.createUser("");
        SignUpRequestDTO signUpRequestDTO = SignUpRequestDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password("password")
                .build();

        restUserMockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(signUpRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Login registered user
        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .email(user.getEmail())
                .password("password")
                .build();

        MvcResult loginMvcResult = restUserMockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(loginRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();

        JWTTokenDTO jwtTokenDTO = JacksonUtil.deserialize(loginMvcResult.getResponse().getContentAsString(),
                new TypeReference<JWTTokenDTO>() {});

        // Check if user can authenticate with access token
        restUserMockMvc.perform(get("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenDTO.getAccessToken());

        MvcResult mvcResult = restUserMockMvc.perform(
                get("/api/v1/users/me").headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDTO userResponseDTO = JacksonUtil.deserialize(mvcResult.getResponse().getContentAsString(),
                new TypeReference<UserResponseDTO>() {});

        assertThat(userResponseDTO.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResponseDTO.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userResponseDTO.getLastName()).isEqualTo(user.getLastName());

        // Assume access token is expired let's try to get new access token by refresh
        JWTRefreshDTO jwtRefreshDTO = JWTRefreshDTO.builder()
                .refreshToken(jwtTokenDTO.getRefreshToken())
                .build();

        MvcResult jwtRefreshMvcResult = restUserMockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(jwtRefreshDTO)))
                .andExpect(status().isOk())
                .andReturn();

        JWTTokenDTO refreshedJwtTokenDTO = JacksonUtil.deserialize(jwtRefreshMvcResult.getResponse().getContentAsString(),
                new TypeReference<JWTTokenDTO>() {});

        HttpHeaders refreshedTokenHeaders = new HttpHeaders();
        refreshedTokenHeaders.setBearerAuth(refreshedJwtTokenDTO.getAccessToken());

        MvcResult mvcRefreshedTokensResult = restUserMockMvc.perform(
                        get("/api/v1/users/me").headers(refreshedTokenHeaders)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDTO userRefreshedResponseDTO = JacksonUtil.deserialize(mvcRefreshedTokensResult.getResponse().getContentAsString(),
                new TypeReference<UserResponseDTO>() {});

        assertThat(userRefreshedResponseDTO.getEmail()).isEqualTo(user.getEmail());
        assertThat(userRefreshedResponseDTO.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userRefreshedResponseDTO.getLastName()).isEqualTo(user.getLastName());
    }

}