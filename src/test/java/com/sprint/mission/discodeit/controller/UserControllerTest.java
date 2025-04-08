package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  UserService userService;

  @MockitoBean
  UserStatusService userStatusService;

  @Test
  @DisplayName("유저 생성 테스트")
  void createUserTest() throws Exception {
    UUID userId = UUID.randomUUID();
    UserDto mockUserDto = new UserDto(userId, "newUser", "new@example.com", null, false);

    given(userService.create(any(), any())).willReturn(mockUserDto);

    UserCreateRequest request = new UserCreateRequest("newUser", "new@example.com", "password123");

    MockMultipartFile requestPart = new MockMultipartFile("userCreateRequest", null,
        MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));

    MockMultipartFile profileFile = new MockMultipartFile("profile", "profile.png",
        MediaType.IMAGE_PNG_VALUE, "dummy image content".getBytes(StandardCharsets.UTF_8));

    mockMvc.perform(multipart("/api/users")
            .file(requestPart)
            .file(profileFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("newUser"))
        .andExpect(jsonPath("$.email").value("new@example.com"));
  }

  @Test
  @DisplayName("유저 업데이트 테스트")
  void updateUserTest() throws Exception {
    UUID userId = UUID.randomUUID();
    UserDto mockUserDto = new UserDto(userId, "updatedUser", "updated@example.com", null, true);

    given(userService.update(eq(userId), any(), any())).willReturn(mockUserDto);

    UserUpdateRequest updateRequest = new UserUpdateRequest("updatedUser", "updated@example.com", "updatedPassword123");

    MockMultipartFile requestPart = new MockMultipartFile("userUpdateRequest", null,
        MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(updateRequest));

    MockMultipartFile profileFile = new MockMultipartFile("profile", "new-profile.png",
        MediaType.IMAGE_PNG_VALUE, "updated image content".getBytes(StandardCharsets.UTF_8));

    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(requestPart)
            .file(profileFile)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("updatedUser"))
        .andExpect(jsonPath("$.email").value("updated@example.com"))
        .andExpect(jsonPath("$.online").value(true));
  }

  @Test
  @DisplayName("유저 삭제 테스트")
  void deleteUserTest() throws Exception {
    UUID userId = UUID.randomUUID();
    doNothing().when(userService).delete(userId);

    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("모든 유저 조회 테스트")
  void findAllUsersTest() throws Exception {
    List<UserDto> users = Arrays.asList(
        new UserDto(UUID.randomUUID(), "user1", "user1@example.com", null, false),
        new UserDto(UUID.randomUUID(), "user2", "user2@example.com", null, true)
    );

    given(userService.findAll()).willReturn(users);

    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(2))
        .andExpect(jsonPath("$[0].username").value("user1"))
        .andExpect(jsonPath("$[1].username").value("user2"));
  }
}