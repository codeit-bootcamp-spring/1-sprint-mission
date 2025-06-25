package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User.Role;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.user.UserNameDuplicateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  UserService userService;

  @Test
  @DisplayName("createUserWithProfile")
  void createUserWithProfile() throws Exception {
    UserCreateRequest request = new UserCreateRequest("email@email.com", "username",
        "password");
    MockMultipartFile userCreateRequest = new MockMultipartFile("userCreateRequest", null,
        "application/json", objectMapper.writeValueAsBytes(request));
    MockMultipartFile profile = new MockMultipartFile("profile", "profile.jpg",
        "imege/jpeg", "file".getBytes());

    BinaryContentDto binaryContentDto = new BinaryContentDto(UUID.randomUUID(),
        profile.getSize(), profile.getName(), profile.getContentType());

    UserDto userDto = new UserDto(UUID.randomUUID(), request.username(),
        request.email(), binaryContentDto, false, Role.USER);

    given(userService.createUser(any(), any())).willReturn(userDto);

    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequest)
            .file(profile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userDto.id().toString()))
        .andExpect(jsonPath("$.username").value(userDto.username()));
  }

  @Test
  @DisplayName("createUser")
  void createUser() throws Exception {
    UserCreateRequest request = new UserCreateRequest("email@email.com", "username",
        "password");
    MockMultipartFile userCreateRequest = new MockMultipartFile("userCreateRequest", null,
        "application/json", objectMapper.writeValueAsBytes(request));

    UserDto userDto = new UserDto(UUID.randomUUID(), request.username(),
        request.email(), null, false, Role.USER);

    given(userService.createUser(any(), any())).willReturn(userDto);

    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequest)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userDto.id().toString()))
        .andExpect(jsonPath("$.username").value(userDto.username()));
  }

  @Test
  @DisplayName("createUser 실패 - 이름이 중복된 경우")
  void failCreateUser() throws Exception {
    UserCreateRequest request = new UserCreateRequest("email@email.com", "username",
        "password");
    MockMultipartFile userCreateRequest = new MockMultipartFile("userCreateRequest", null,
        "application/json", objectMapper.writeValueAsBytes(request));

    UserDto userDto = new UserDto(UUID.randomUUID(), request.username(),
        request.email(), null, false, Role.USER);

    given(userService.createUser(any(), any())).willThrow(new UserNameDuplicateException(Map.of()));

    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequest)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_USER_USERNAME.getMessage()));
  }

  @Test
  @DisplayName("getUsers")
  void getUsers() throws Exception {

    UserDto userDto1 = new UserDto(UUID.randomUUID(), "username1",
        "email1@email.com", null, false, Role.USER);
    UserDto userDto2 = new UserDto(UUID.randomUUID(), "username2",
        "email2@email.com", null, false, Role.USER);

    given(userService.readAll()).willReturn(List.of(userDto1, userDto2));

    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2)) // JSON 배열 크기
        .andExpect(jsonPath("$[0].username").value("username1"))
        .andExpect(jsonPath("$[1].email").value("email2@email.com"));
  }

  @Test
  @DisplayName("updateUserWithProfile")
  void updateUserWithProfile() throws Exception {
    UserUpdateRequest request = new UserUpdateRequest("email@email.com", "username",
        "password");
    MockMultipartFile userUpdateRequest = new MockMultipartFile("userUpdateRequest", null,
        "application/json", objectMapper.writeValueAsBytes(request));
    MockMultipartFile profile = new MockMultipartFile("profile", "profile.jpg",
        "imege/jpeg", "file".getBytes());

    BinaryContentDto binaryContentDto = new BinaryContentDto(UUID.randomUUID(),
        profile.getSize(), profile.getName(), profile.getContentType());

    UserDto userDto = new UserDto(UUID.randomUUID(), request.newUsername(),
        request.newEmail(), binaryContentDto, false, Role.USER);

    given(userService.updateUser(any(), any(), any())).willReturn(userDto);

    mockMvc.perform(multipart("/api/users/" + userDto.id())
            .file(userUpdateRequest)
            .file(profile)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userDto.id().toString()))
        .andExpect(jsonPath("$.username").value(userDto.username()));
  }

  @Test
  @DisplayName("updateUser 실패 - id가 없는 경우")
  void failUpdateUser() throws Exception {
    UserUpdateRequest request = new UserUpdateRequest("email@email.com", "username",
        "password");
    MockMultipartFile userUpdateRequest = new MockMultipartFile("userUpdateRequest", null,
        "application/json", objectMapper.writeValueAsBytes(request));

    UserDto userDto = new UserDto(UUID.randomUUID(), request.newUsername(),
        request.newEmail(), null, false, Role.USER);

    given(userService.updateUser(any(), any(), any())).willThrow(
        new UserNotFoundException(Map.of()));

    mockMvc.perform(multipart("/api/users/" + userDto.id())
            .file(userUpdateRequest)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("deleteUser")
  void deleteUser() throws Exception {
    UUID userId = UUID.randomUUID();

    mockMvc.perform(delete("/api/users/" + userId))
        .andExpect(status().isNoContent());
  }
}