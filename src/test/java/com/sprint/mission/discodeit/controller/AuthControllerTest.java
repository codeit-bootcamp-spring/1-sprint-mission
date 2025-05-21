package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private AuthService authService;

  @Test
  @DisplayName("로그인 성공 테스트")
  void login_Success() throws Exception {
    // Given
    LoginRequest loginRequest = new LoginRequest(
        "testuser",
        "Password1!"
    );

    UUID userId = UUID.randomUUID();
    UserDto loggedInUser = new UserDto(
        userId,
        "testuser",
        "test@example.com",
        null,
        true
    );

    given(authService.login(any(LoginRequest.class))).willReturn(loggedInUser);

    // When & Then
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.online").value(true));
  }

  @Test
  @DisplayName("로그인 실패 테스트 - 존재하지 않는 사용자")
  void login_Failure_UserNotFound() throws Exception {
    // Given
    LoginRequest loginRequest = new LoginRequest(
        "nonexistentuser",
        "Password1!"
    );

    given(authService.login(any(LoginRequest.class)))
        .willThrow(UserNotFoundException.withUsername("nonexistentuser"));

    // When & Then
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("로그인 실패 테스트 - 잘못된 비밀번호")
  void login_Failure_InvalidCredentials() throws Exception {
    // Given
    LoginRequest loginRequest = new LoginRequest(
        "testuser",
        "WrongPassword1!"
    );

    given(authService.login(any(LoginRequest.class)))
        .willThrow(InvalidCredentialsException.wrongPassword());

    // When & Then
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("로그인 실패 테스트 - 유효하지 않은 요청")
  void login_Failure_InvalidRequest() throws Exception {
    // Given
    LoginRequest invalidRequest = new LoginRequest(
        "", // 사용자 이름 비어있음 (NotBlank 위반)
        ""  // 비밀번호 비어있음 (NotBlank 위반)
    );

    // When & Then
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());
  }
} 