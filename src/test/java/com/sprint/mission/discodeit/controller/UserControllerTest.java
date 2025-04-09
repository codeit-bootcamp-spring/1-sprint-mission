package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
@Import(ValidationAutoConfiguration.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc; // HTTP 요청을 수행하는 MockMvc

  @Autowired
  private ObjectMapper objectMapper; // JSON 직렬화를 위한 ObjectMapper

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserStatusService userStatusService;

  // --- [사용자 생성 테스트] ---

  @Test
  @DisplayName("[성공] 사용자 생성")
  void createUser_success() throws Exception {
    // Given
    UserCreateRequest request = new UserCreateRequest("username", "email@email.com", "password");
    UserDto response = new UserDto(UUID.randomUUID(), "username", "email@email.com", null, true);

    // When
    when(userService.create(any(), any())).thenReturn(response);
    MockMultipartFile userCreateRequestPart = new MockMultipartFile(
        "userCreateRequest", null,
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    // Then
    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
            .file(userCreateRequestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username"));
  }

  @Test
  @DisplayName("[실패] 사용자 생성 - 필수 필드 누락")
  void createUser_fail() throws Exception {
    // Given
    UserCreateRequest request = new UserCreateRequest(null, "email@email.com", "password");
    MockMultipartFile userCreateRequestPart = new MockMultipartFile(
        "userCreateRequest", null,
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
            .file(userCreateRequestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("[성공] 사용자 수정")
  void updateUser_success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("updatedUser", "updated@email.com",
        "updatedPassword");
    UserDto response = new UserDto(userId, "updatedUser", "updated@email.com", null, true);

    // When
    when(userService.update(eq(userId), any(), any())).thenReturn(response);
    MockMultipartFile userUpdateRequestPart = new MockMultipartFile(
        "userUpdateRequest", null,
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    // Then
    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users/" + userId)
            .file(userUpdateRequestPart)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("updatedUser"));
  }

  @Test
  @DisplayName("[실패] 사용자 수정 - 유효성 실패")
  void updateUser_fail_validation() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest(null, null, null);
    MockMultipartFile userUpdateRequestPart = new MockMultipartFile(
        "userUpdateRequest", null,
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users/" + userId)
            .file(userUpdateRequestPart)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("[성공] 사용자 삭제")
  void deleteUser_success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();

    // When
    doNothing().when(userService).delete(userId);

    // Then
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + userId))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  // --- [사용자 목록 조회 테스트] ---

  @Test
  @DisplayName("[성공] 사용자 목록 조회")
  void findAllUsers_success() throws Exception {
    // Given
    UserDto user1 = new UserDto(UUID.randomUUID(), "user1", "email1@email.com", null, true);
    UserDto user2 = new UserDto(UUID.randomUUID(), "user2", "email2@email.com", null, false);

    // When
    when(userService.findAll()).thenReturn(List.of(user1, user2));

    // Then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("user1"));
  }

  @Test
  @DisplayName("[성공] 사용자 상태 수정")
  void updateUserStatus_success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    Instant now = Instant.now();
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(now);
    UserStatusDto response = new UserStatusDto(UUID.randomUUID(), userId, now);

    // When
    when(userStatusService.updateByUserId(eq(userId), any())).thenReturn(response);

    // Then
    mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/" + userId + "/userStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastActiveAt").exists());
  }

  @Test
  @DisplayName("[실패] 사용자 상태 수정 - 유효하지 않은 요청")
  void updateUserStatus_fail_invalid() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(null);

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/" + userId + "/userStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
