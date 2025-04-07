package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

  @MockBean
  private UserStatusService userStatusService;

  @Test
  void createUser_Success() throws Exception {
    // given
    UserCreateRequest request = new UserCreateRequest("test", "test@gmail.com", "qwer1234!");
    UserDto createdDto = new UserDto(UUID.randomUUID(), request.username(), request.email(), null,
        null);
    MockMultipartFile userCreateRequestFile = new MockMultipartFile(
        "userCreateRequest", "userCreateRequest.json", "application/json",
        objectMapper.writeValueAsBytes(request));
    MockMultipartFile profile = new MockMultipartFile(
        "profile", "originalFile", "text/plain", "userProfile".getBytes());

    given(userService.create(any(UserCreateRequest.class), any())).willReturn(createdDto);

    // when, then
    mockMvc.perform(
            multipart("/api/users")
                .file(userCreateRequestFile)
                .file(profile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.username").value("test"))
        .andExpect(jsonPath("$.email").value("test@gmail.com"));
  }

  @Test
  void createUser_Fail_InvalidEmail() throws Exception {
    // given
    UserCreateRequest request = new UserCreateRequest("test", "test", "qwer1234!");
    MockMultipartFile userCreateRequestFile = new MockMultipartFile(
        "userCreateRequest", "userCreateRequest.json", "application/json",
        objectMapper.writeValueAsBytes(request));
    MockMultipartFile profile = new MockMultipartFile(
        "profile", "originalFile", "text/plain", "userProfile".getBytes());

    // when, then
    mockMvc.perform(
            multipart("/api/users")
                .file(userCreateRequestFile)
                .file(profile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.status").value(400));
  }

  @Test
  void update_Success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("newTest", "new@gmail.com", "qwer1233!");
    UserDto userDto = new UserDto(userId, request.newUsername(), request.newEmail(), null, null);
    MockMultipartFile requestFile = new MockMultipartFile("userUpdateRequest",
        "userUpdateRequest.json", "application/json",
        objectMapper.writeValueAsBytes(request));
    MockMultipartFile profile = new MockMultipartFile("profile", "test.txt", "text/plain",
        "test".getBytes());

    given(userService.update(any(UUID.class), any(UserUpdateRequest.class), any())).willReturn(
        userDto);

    // when, then
    mockMvc.perform(
            multipart("/api/users/{userId}", userId)
                .file(requestFile)
                .file(profile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .with(servletRequest -> {
                  servletRequest.setMethod("PATCH"); // 요청 메서드를 PATCH로 강제 지정
                  return servletRequest;
                })
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.username").value("newTest"))
        .andExpect(jsonPath("$.email").value("new@gmail.com"));
  }

  @Test
  void deleteUser_success() throws Exception {
    UUID userId = UUID.randomUUID();

    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());
  }

  @Test
  void updateUserStatus_success() throws Exception {
    UUID userId = UUID.randomUUID();
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(Instant.now());
    UserStatusDto responseDto = new UserStatusDto(UUID.randomUUID(), userId,
        request.newLastActiveAt());

    when(userStatusService.updateByUserId(eq(userId),
        any(UserStatusUpdateRequest.class))).thenReturn(responseDto);

    mockMvc.perform(
            patch("/api/users/{userId}/userStatus", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(userId.toString()))
        .andExpect(jsonPath("$.lastActiveAt").value(request.newLastActiveAt().toString()));
  }
}

