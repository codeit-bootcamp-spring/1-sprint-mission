package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.service.Interface.UserService;
import com.sprint.mission.discodeit.service.Interface.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserStatusService userStatusService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createUser() throws Exception {
    UserCreateRequestDto request = new UserCreateRequestDto("홍길동", "hong@naver.com", "1234");

    UserDto responseDto = new UserDto();
    responseDto.setId(UUID.randomUUID());
    responseDto.setUsername("홍길동");
    responseDto.setEmail("hong@naver.com");
    responseDto.setOnline(true);

    given(userService.createUser(ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(
        responseDto);

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(request));
    MockMultipartFile profile = new MockMultipartFile(
        "profile",
        "profile.png",
        "image/png",
        "dummy-image".getBytes());

    mvc.perform(multipart("/api/users")
            .file(jsonPart)
            .file(profile)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("홍길동"))
        .andExpect(jsonPath("$.email").value("hong@naver.com"))
        .andExpect(jsonPath("$.online").value(true));
  }

  @Test
  void 사용자_생성_실패() throws Exception {
    // given
    MockMultipartFile invalidJson = new MockMultipartFile(
        "userCreateRequest",
        "",
        "application/json",
        "{}".getBytes()
    );

    mvc.perform(multipart("/api/users")
            .file(invalidJson)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void findAll() throws Exception {
    UserDto dto = new UserDto();
    dto.setId(UUID.randomUUID());
    dto.setUsername("user1");
    dto.setEmail("user1@naver.com");
    dto.setOnline(true);
    UserDto dto2 = new UserDto();
    dto2.setId(UUID.randomUUID());
    dto2.setUsername("user2");
    dto2.setEmail("user2@naver.com");
    dto2.setOnline(true);

    given(userService.getAllUsers()).willReturn(List.of(dto, dto2));
    mvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void updateUser() throws Exception {
    UUID id = UUID.randomUUID();
    UserUpdateRequestDto request = new UserUpdateRequestDto("newName", "new@email.com", "");
    UserDto responseDto = new UserDto();
    responseDto.setId(id);
    responseDto.setUsername("newName");
    responseDto.setEmail("new@email.com");

    MockMultipartFile userJson = new MockMultipartFile(
        "userUpdateRequest",
        null,
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    MockMultipartFile profile = new MockMultipartFile(
        "profile",
        "profile.png",
        "image/png",
        "dummy-image".getBytes());

    given(userService.updateUser(eq(id), any(), any())).willReturn(responseDto);

    mvc.perform(multipart("/api/users/" + id)
            .file(userJson)
            .file(profile)
            .with(request1 -> {
              request1.setMethod("PATCH");
              return request1;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("newName"))
        .andExpect(jsonPath("$.email").value("new@email.com"));
  }

  @Test
  void 유저_수정_존재하지_않는_아이디() throws Exception {
    UUID id = UUID.randomUUID();
    UserUpdateRequestDto request = new UserUpdateRequestDto("name", "email@email.com", "");

    given(userService.updateUser(eq(id), any(), any()))
        .willThrow(new IllegalArgumentException("존재하지 않는 사용자입니다."));

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userUpdateRequest", "", "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    mvc.perform(multipart("/api/users/" + id)
            .file(jsonPart)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("존재하지 않는 사용자입니다."));
  }

  @Test
  void deleteUser() throws Exception {
    UUID id = UUID.randomUUID();
    mvc.perform(delete("/api/users/" + id)).andExpect(status().isNoContent());
  }

  @Test
  void updateUserStatusByUserId() throws Exception {
    UUID id = UUID.randomUUID();
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(Instant.now());
    UserStatus status = new UserStatus();
    status.setLastActiveAt(Instant.now());

    given(userStatusService.updateByUserId(eq(id), any())).willReturn(status);

    mvc.perform(patch("/api/users/{userId}/userStatus", id)
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void 상태_갱신_서버_에러() throws Exception {
    UUID id = UUID.randomUUID();
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(Instant.now());

    given(userStatusService.updateByUserId(eq(id), any()))
        .willThrow(new RuntimeException("예상치 못한 오류"));

    mvc.perform(patch("/api/users/{userId}/userStatus", id)
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("서버 내부 오류가 발생했습니다.")); // GlobalExceptionHandler 기준
  }
}