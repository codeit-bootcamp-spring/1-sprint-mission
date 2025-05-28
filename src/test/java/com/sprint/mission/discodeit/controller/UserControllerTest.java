package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.TestConfig;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateResponse;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@Import(TestConfig.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("POST /api/users - 유저 생성")
  void createUser() throws Exception {
    UserCreateRequest request = new UserCreateRequest("user1", "user1@mail.com", "password");
    String requestJson = objectMapper.writeValueAsString(request);

    UUID userId = UUID.randomUUID();
    UserCreateResponse userResponse = new UserCreateResponse(userId, "user1", "user1@mail.com",
        null,
        true);

    when(userService.join(any(UserCreateRequest.class), isNull()))
        .thenReturn(userResponse);

    MockPart mockFile = new MockPart("userCreateRequest", null, requestJson.getBytes());
    mockFile.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(multipart("/api/users")
            .part(mockFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(
            MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(userResponse)));

    verify(userService).join(any(UserCreateRequest.class), isNull());
  }


  @Test
  @DisplayName("POST /api/users - 중복 이름으로 인한 유저 생성 실패")
  void createUserFailByUsername() throws Exception {
    UserCreateRequest request = new UserCreateRequest("user", "user@mail.com", "password");
    String requestJson = objectMapper.writeValueAsString(request);

    DuplicateUsernameException exception = new DuplicateUsernameException(
        Instant.now(),
        ErrorCode.EXIST_USER,
        Map.of("user", ErrorCode.EXIST_USER.getMessage())
    );

    when(userService.join(any(UserCreateRequest.class), isNull()))
        .thenThrow(exception);

    MockPart mockFile = new MockPart("userCreateRequest", null, requestJson.getBytes());
    mockFile.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(multipart("/api/users")
            .part(mockFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isConflict());

    verify(userService).join(any(UserCreateRequest.class), isNull());
  }

  @Test
  @DisplayName("POST /api/users - 중복 이메일으로 인한 유저 생성 실패")
  void createUserFailByEmail() throws Exception {
    UserCreateRequest request = new UserCreateRequest("user", "user@mail.com", "password");
    String requestJson = objectMapper.writeValueAsString(request);

    DuplicateEmailException exception = new DuplicateEmailException(
        Instant.now(),
        ErrorCode.EXIST_USER,
        Map.of("user", ErrorCode.EXIST_USER.getMessage())
    );

    when(userService.join(any(UserCreateRequest.class), isNull()))
        .thenThrow(exception);

    MockPart mockFile = new MockPart("userCreateRequest", null, requestJson.getBytes());
    mockFile.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(multipart("/api/users")
            .part(mockFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isConflict());

    verify(userService).join(any(UserCreateRequest.class), isNull());
  }

  @Test
  @DisplayName("DELETE /api/users/{userId} - 유저 삭제")
  void deleteUser() throws Exception {
    UUID userId = UUID.randomUUID();

    when(userService.delete(userId))
        .thenReturn(userId);

    mockMvc
        .perform(delete("/api/users/{userId}", userId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(userService).delete(userId);
  }

  @Test
  @DisplayName("PATCH /api/users/{userId} - 유저 정보 수정")
  void modifyUser() throws Exception {
    UserUpdateRequest request = new UserUpdateRequest("newUser", "new@mail.com", "newpassword");
    UUID userId = UUID.randomUUID();
    String requestJson = objectMapper.writeValueAsString(request);

    UserUpdateResponse response = new UserUpdateResponse(userId, request.newUsername(),
        request.newEmail(),
        null, true);

    when(userService.update(any(UUID.class), any(UserUpdateRequest.class), isNull()))
        .thenReturn(response);

    MockPart mockFile = new MockPart("userUpdateRequest", null, requestJson.getBytes());
    mockFile.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(multipart(HttpMethod.PATCH, "/api/users/{userId}", userId)
            .part(mockFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));

    verify(userService).update(userId, request, null);
  }

  @Test
  @DisplayName("PATCH /api/users/{userId} - 유저 정보 수정 실패")
  void modifyUserFail() throws Exception {
    UserUpdateRequest request = new UserUpdateRequest("user", "user@mail.com", "password");
    String requestJson = objectMapper.writeValueAsString(request);
    UUID userId = UUID.randomUUID();

    UserNotFoundException exception = new UserNotFoundException(
        Instant.now(),
        ErrorCode.EXIST_USER,
        Map.of("user", ErrorCode.USER_NOT_FOUND.getMessage())
    );

    when(userService.update(any(UUID.class), any(UserUpdateRequest.class), isNull()))
        .thenThrow(exception);

    MockPart mockFile = new MockPart("userUpdateRequest", null, requestJson.getBytes());
    mockFile.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(multipart(HttpMethod.PATCH, "/api/users/{userId}", userId)
            .part(mockFile)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    verify(userService).update(any(UUID.class), any(UserUpdateRequest.class),
        any());
  }
}