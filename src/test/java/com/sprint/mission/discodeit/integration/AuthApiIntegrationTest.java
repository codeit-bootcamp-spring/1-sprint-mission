package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthApiIntegrationTest {

  private static final Logger logger = LoggerFactory.getLogger(AuthApiIntegrationTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserService userService;

  @Test
  @DisplayName("로그인 API 통합 테스트 - 성공")
  void login_Success() throws Exception {
    logger.info("==== 로그인 API 통합 테스트 - 성공 시작 ====");

    // Given
    // 테스트 사용자 생성
    UserCreateRequest userRequest = new UserCreateRequest(
        "testuser",
        "testuser@example.com",
        "Password1!"
    );

    userService.create(userRequest, Optional.empty());
    logger.info("테스트 사용자 생성 완료");

    // 로그인 요청
    LoginRequest loginRequest = new LoginRequest(
        "testuser",
        "Password1!"
    );

    String requestBody = objectMapper.writeValueAsString(loginRequest);
    logger.info("로그인 요청 준비 완료");

    // When & Then
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.username", is("testuser")))
        .andExpect(jsonPath("$.email", is("testuser@example.com")));

    logger.info("==== 로그인 API 통합 테스트 - 성공 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("로그인 API 통합 테스트 - 실패 (존재하지 않는 사용자)")
  void login_Failure_UserNotFound() throws Exception {
    logger.info("==== 로그인 API 통합 테스트 - 실패 (존재하지 않는 사용자) 시작 ====");

    // Given
    LoginRequest loginRequest = new LoginRequest(
        "wronguser",
        "Password1!"
    );

    String requestBody = objectMapper.writeValueAsString(loginRequest);
    logger.info("존재하지 않는 사용자로 로그인 요청 준비 완료");

    // When & Then
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isNotFound());

    logger.info("==== 로그인 API 통합 테스트 - 실패 (존재하지 않는 사용자) 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("로그인 API 통합 테스트 - 실패 (잘못된 비밀번호)")
  void login_Failure_InvalidCredentials() throws Exception {
    logger.info("==== 로그인 API 통합 테스트 - 실패 (잘못된 비밀번호) 시작 ====");

    // Given
    // 테스트 사용자 생성
    UserCreateRequest userRequest = new UserCreateRequest(
        "testuser2",
        "login2@example.com",
        "Password1!"
    );

    userService.create(userRequest, Optional.empty());
    logger.info("테스트 사용자 생성 완료");

    // 잘못된 비밀번호로 로그인 시도
    LoginRequest loginRequest = new LoginRequest(
        "testuser2",
        "WrongPassword1!"
    );

    String requestBody = objectMapper.writeValueAsString(loginRequest);
    logger.info("잘못된 비밀번호로 로그인 요청 준비 완료");

    // When & Then
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isUnauthorized());

    logger.info("==== 로그인 API 통합 테스트 - 실패 (잘못된 비밀번호) 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("로그인 API 통합 테스트 - 실패 (유효하지 않은 요청)")
  void login_Failure_InvalidRequest() throws Exception {
    logger.info("==== 로그인 API 통합 테스트 - 실패 (유효하지 않은 요청) 시작 ====");

    // Given
    LoginRequest invalidRequest = new LoginRequest(
        "", // NotBlank 위반 확인
        ""  // NotBlank 위반 확인
    );

    String requestBody = objectMapper.writeValueAsString(invalidRequest);
    logger.info("유효하지 않은 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isBadRequest());

    logger.info("==== 로그인 API 통합 테스트 - 실패 (유효하지 않은 요청) 완료 ====");
    System.out.println("\n");
  }
}