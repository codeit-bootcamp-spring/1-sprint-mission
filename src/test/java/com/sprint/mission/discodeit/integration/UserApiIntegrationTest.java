package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserApiIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserService userService;


  @Test
  @DisplayName("사용자 생성 API 통합 테스트")
  void createUser_Success() throws Exception {
    // Given
    UserCreateRequest createRequest = new UserCreateRequest(
        "testuser",
        "test@example.com",
        "Password1!"
    );

    MockMultipartFile userCreateRequestPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(createRequest)
    );

    MockMultipartFile profilePart = new MockMultipartFile(
        "profile",
        "profile.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "test-image".getBytes()
    );

    // When & Then
    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequestPart)
            .file(profilePart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.username", is("testuser")))
        .andExpect(jsonPath("$.email", is("test@example.com")))
        .andExpect(jsonPath("$.profile.fileName", is("profile.jpg")))
        .andExpect(jsonPath("$.online", is(true)));
  }

  @Test
  @DisplayName("사용자 생성 실패 API 통합 테스트 - 유효하지 않은 요청")
  void createUser_Failure_InvalidRequest() throws Exception {
    // Given
    UserCreateRequest invalidRequest = new UserCreateRequest(
        "t", // 최소 길이 위반
        "invalid-email", // 이메일 형식 위반
        "short" // 비밀번호 정책 위반
    );

    MockMultipartFile userCreateRequestPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(invalidRequest)
    );

    // When & Then
    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("모든 사용자 조회 API 통합 테스트")
  void findAllUsers_Success() throws Exception {
    // Given
    // 테스트 사용자 생성 - Service를 통해 초기화
    UserCreateRequest userRequest1 = new UserCreateRequest(
        "user1",
        "user1@example.com",
        "Password1!"
    );

    UserCreateRequest userRequest2 = new UserCreateRequest(
        "user2",
        "user2@example.com",
        "Password1!"
    );

    userService.create(userRequest1, Optional.empty());
    userService.create(userRequest2, Optional.empty());

    // When & Then
    mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].username", is("user1")))
        .andExpect(jsonPath("$[0].email", is("user1@example.com")))
        .andExpect(jsonPath("$[1].username", is("user2")))
        .andExpect(jsonPath("$[1].email", is("user2@example.com")));
  }

  @Test
  @DisplayName("사용자 업데이트 API 통합 테스트")
  void updateUser_Success() throws Exception {
    // Given
    // 테스트 사용자 생성 - Service를 통해 초기화
    UserCreateRequest createRequest = new UserCreateRequest(
        "originaluser",
        "original@example.com",
        "Password1!"
    );

    UserDto createdUser = userService.create(createRequest, Optional.empty());
    UUID userId = createdUser.id();

    UserUpdateRequest updateRequest = new UserUpdateRequest(
        "updateduser",
        "updated@example.com",
        "UpdatedPassword1!"
    );

    MockMultipartFile userUpdateRequestPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(updateRequest)
    );

    MockMultipartFile profilePart = new MockMultipartFile(
        "profile",
        "updated-profile.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "updated-image".getBytes()
    );

    // When & Then
    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(userUpdateRequestPart)
            .file(profilePart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(userId.toString())))
        .andExpect(jsonPath("$.username", is("updateduser")))
        .andExpect(jsonPath("$.email", is("updated@example.com")))
        .andExpect(jsonPath("$.profile.fileName", is("updated-profile.jpg")));
  }

  @Test
  @DisplayName("사용자 업데이트 실패 API 통합 테스트 - 존재하지 않는 사용자")
  void updateUser_Failure_UserNotFound() throws Exception {
    // Given
    UUID nonExistentUserId = UUID.randomUUID();
    UserUpdateRequest updateRequest = new UserUpdateRequest(
        "updateduser",
        "updated@example.com",
        "UpdatedPassword1!"
    );

    MockMultipartFile userUpdateRequestPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(updateRequest)
    );

    // When & Then
    mockMvc.perform(multipart("/api/users/{userId}", nonExistentUserId)
            .file(userUpdateRequestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("사용자 삭제 API 통합 테스트")
  void deleteUser_Success() throws Exception {
    // Given
    // 테스트 사용자 생성 - Service를 통해 초기화
    UserCreateRequest createRequest = new UserCreateRequest(
        "deleteuser",
        "delete@example.com",
        "Password1!"
    );

    UserDto createdUser = userService.create(createRequest, Optional.empty());
    UUID userId = createdUser.id();

    // When & Then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());

    // 삭제 확인
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.id == '" + userId + "')]").doesNotExist());
  }

  @Test
  @DisplayName("사용자 삭제 실패 API 통합 테스트 - 존재하지 않는 사용자")
  void deleteUser_Failure_UserNotFound() throws Exception {
    // Given
    UUID nonExistentUserId = UUID.randomUUID();

    // When & Then
    mockMvc.perform(delete("/api/users/{userId}", nonExistentUserId))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("사용자 상태 업데이트 API 통합 테스트")
  void updateUserStatus_Success() throws Exception {
    // Given
    // 테스트 사용자 생성 - Service를 통해 초기화
    UserCreateRequest createRequest = new UserCreateRequest(
        "statususer",
        "status@example.com",
        "Password1!"
    );

    UserDto createdUser = userService.create(createRequest, Optional.empty());
    UUID userId = createdUser.id();

    Instant newLastActiveAt = Instant.now();
    UserStatusUpdateRequest statusUpdateRequest = new UserStatusUpdateRequest(
        newLastActiveAt
    );
    String requestBody = objectMapper.writeValueAsString(statusUpdateRequest);

    // When & Then
    mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lastActiveAt", is(newLastActiveAt.toString())));
  }

  @Test
  @DisplayName("사용자 상태 업데이트 실패 API 통합 테스트 - 존재하지 않는 사용자")
  void updateUserStatus_Failure_UserNotFound() throws Exception {
    // Given
    UUID nonExistentUserId = UUID.randomUUID();
    UserStatusUpdateRequest statusUpdateRequest = new UserStatusUpdateRequest(
        Instant.now()
    );
    String requestBody = objectMapper.writeValueAsString(statusUpdateRequest);

    // When & Then
    mockMvc.perform(patch("/api/users/{userId}/userStatus", nonExistentUserId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isNotFound());
  }
} 