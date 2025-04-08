package com.sprint.mission.discodeit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.auth.RequestLogin;
import com.sprint.mission.discodeit.entity.auth.ResponseLogin;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserAuthException;
import com.sprint.mission.discodeit.service.auth.UserAuthService;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserAuthController.class)
class UserAuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserAuthService userAuthService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("POST /api/auth/login - 로그인 성공")
  void login() throws Exception {
    RequestLogin request = new RequestLogin("user", "password");
    String requestJson = objectMapper.writeValueAsString(request);

    UUID userId = UUID.randomUUID();
    ResponseLogin response = new ResponseLogin(userId, "user", "user@mail.com", null, true);

    when(userAuthService.login(request))
        .thenReturn(response);

    mockMvc
        .perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(
            content().json(objectMapper.writeValueAsString(response))
        );
  }

  @Test
  @DisplayName("POST /api/auth/login - 로그인 실패")
  void loginFail() throws Exception {
    RequestLogin request = new RequestLogin("user", "password");
    String requestJson = objectMapper.writeValueAsString(request);

    UUID userId = UUID.randomUUID();
    ResponseLogin response = new ResponseLogin(userId, "user", "user@mail.com", null, true);

    UserAuthException exception = new UserAuthException(
        Instant.now(),
        ErrorCode.EXIST_USER,
        Map.of("user", ErrorCode.USER_AUTH_FAIL.getMessage())
    );

    when(userAuthService.login(request))
        .thenThrow(exception);

    mockMvc
        .perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isBadRequest());
  }
}