package com.sprint.mission.discodeit.controller;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.security.auth.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.facade.user.UserFacade;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

@WebMvcTest(
    controllers = UserController.class
)
public class UserControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private UserFacade userFacade;

  @Test
  @DisplayName("GET /api/users/{id} - 성공")
  void getUser_success() throws Exception {
    String userId = UUID.randomUUID().toString();
    UserDto mockResponse = new UserDto(
        UUID.fromString(userId),
        "testuser", "test@example.com", null, true, UserRole.ROLE_USER);

    when(userFacade.findUserById(userId)).thenReturn(mockResponse);

    mockMvc.perform(get("/api/users/" + userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId))
        .andExpect(jsonPath("$.username").value("testuser"));
  }

  @Test
  @DisplayName("GET /api/users/{id} - 실패 (UserNotFound)")
  void getUser_fail() throws Exception {
    String id = UUID.randomUUID().toString();

    given(userFacade.findUserById(id)).willThrow(new UserNotFoundException(
        ErrorCode.USER_NOT_FOUND));

    mockMvc.perform(get("/api/users/" + id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage())
        );
  }

  @Test
  @DisplayName("POST /api/users - 성공")
  void createUser_success() throws Exception {
    CreateUserRequest createUserRequest = new CreateUserRequest("u1", "pw1234", "e@gmail.com");
    String jsonRequest = objectMapper.writeValueAsString(createUserRequest);

    MockMultipartFile userCreatePart = new MockMultipartFile(
        "userCreateRequest", "", "application/json", jsonRequest.getBytes(StandardCharsets.UTF_8)
    );

    MockMultipartFile profile = new MockMultipartFile(
        "profile", "profile.png", "image/jpeg", "profile".getBytes()
    );

    String userId = UUID.randomUUID().toString();
    UserDto dto = new UserDto(UUID.fromString(userId), createUserRequest.username(),
        createUserRequest.email(), null, true, UserRole.ROLE_USER);
    given(userFacade.createUser(createUserRequest, profile)).willReturn(dto);

    mockMvc.perform(multipart("/api/users").file(userCreatePart).file(profile).contentType(
            MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userId))
        .andExpect(jsonPath("$.username").value("u1"));
  }

  @Test
  @DisplayName("POST /api/users - 실패 (잘못된 입력)")
  void createUser_fail() throws Exception {

    CreateUserRequest createUserRequest = new CreateUserRequest("newuser", "",
        "new@gmail.com");

    String jsonRequest = objectMapper.writeValueAsString(createUserRequest);

    MockMultipartFile userCreatePart = new MockMultipartFile(
        "userCreateRequest", "", "application/json", jsonRequest.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(
            multipart("/api/users").file(userCreatePart).contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_FAILED.getCode())
        );
  }

  @Test
  @DisplayName("GET /api/users - 성공(목록 조회)")
  void getUsers_success() throws Exception {
    UserDto response1 = new UserDto(
        UUID.randomUUID(), "u1", "u1@gmail.com", null, true, UserRole.ROLE_USER
    );
    UserDto response2 = new UserDto(
        UUID.randomUUID(), "u2", "u2@gmail.com", null, true, UserRole.ROLE_USER
    );
    List<UserDto> users = List.of(response1, response2);
    when(userFacade.findAllUsers()).thenReturn(users);

    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(users.size()))
        .andExpect(jsonPath("$.[0].username").value("u1"))
        .andExpect(jsonPath("$.[1].username").value("u2"));
  }

  // TODO : getUsers 실패

  @Test
  @DisplayName("PATCH /api/users/{id} - 성공")
  void updateUsers_success() throws Exception {
    String userId = UUID.randomUUID().toString();
    UserUpdateDto updateDto = new UserUpdateDto("updated", "new@gmail.com", "newpwd");
    String jsonUpdateDto = objectMapper.writeValueAsString(updateDto);
    MockMultipartFile userUpdatePart = new MockMultipartFile(
        "userUpdateRequest", null, "application/json",
        jsonUpdateDto.getBytes(StandardCharsets.UTF_8)
    );
    MockMultipartFile newProfile = new MockMultipartFile(
        "profile", "newProfile.jpg", "image/jpeg", "new".getBytes()
    );
    UserDto response = new UserDto(
        UUID.fromString(userId),
        updateDto.newUsername(),
        updateDto.newEmail(),
        null,
        true,
        UserRole.ROLE_USER
    );

    UserDetails details = mock(DiscodeitUserDetails.class);

    given(userFacade.updateUser(userId, newProfile, updateDto, details))
        .willReturn(response);

    MockMultipartHttpServletRequestBuilder builder = multipart("/api/users/" + userId);
    builder.with(request -> {
      request.setMethod("PATCH");
      return request;
    });

    mockMvc.perform(
            builder.file(userUpdatePart).file(newProfile).contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId))
        .andExpect(jsonPath("$.username").value("updated"));
  }

  // TODO : updateUser 실패

  @Test
  @DisplayName("DELETE /api/users/{id} - success")
  void deleteUser_success() throws Exception {
    String userId = UUID.randomUUID().toString();

    mockMvc.perform(delete("/api/users/" + userId))
        .andExpect(status().isNoContent());
  }
}
