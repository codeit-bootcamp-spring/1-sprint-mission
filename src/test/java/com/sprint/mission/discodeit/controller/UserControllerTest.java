package com.sprint.mission.discodeit.controller;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

//@Import({ErrorCodeStatusMapper.class})
@WebMvcTest(UserController.class)
//@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserStatusService userStatusService;

  @Autowired
  private ObjectMapper objectMapper;

  @DisplayName("/api/users - 프로필 사진 없이 유저를 생성한다.")
  @Test
  void createUser() throws Exception {
    //given
    UserCreateDTO request = new UserCreateDTO("testuser", "test@abc.com", "password123");

    UserDto response = new UserDto(UUID.randomUUID(), request.getUsername(), request.getEmail(),
        null, true);

    MockMultipartFile userCreateRequest = new MockMultipartFile(
        "userCreateRequest", // @RequestPart 이름과 일치하게
        null, // 파일 이름
        "application/json", //content type
        new ObjectMapper().writeValueAsBytes(request) //실제 내용 (바이트 배열)
    );

    given(userService.create(any(), any())).willReturn(response);

    //when then
    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequest)
            .contentType(MediaType.MULTIPART_FORM_DATA)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.email").value("test@abc.com"));
  }

  @DisplayName("/api/users - 프로필 사진을 가진 유저를 생성한다.")
  @Test
  void createUser_withProfileImage() throws Exception {
    // given
    UserCreateDTO request = new UserCreateDTO("testuser", "test@abc.com", "password123");

    UserDto response = new UserDto(
        UUID.randomUUID(), "testuser", "test@abc.com", null, true
    );

    // JSON DTO 파트
    MockMultipartFile userCreateRequest = new MockMultipartFile(
        "userCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    // 프로필 이미지 파트 (사진 파일)
    MockMultipartFile profile = new MockMultipartFile(
        "profile",
        "profile.png",             // 파일 이름
        "image/png",               // MIME 타입
        "dummy-image-content".getBytes() // 파일 내용 (바이트 배열)
    );

    given(userService.create(any(), any())).willReturn(response);

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequest)
            .file(profile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.email").value("test@abc.com"))
        .andExpect(jsonPath("$.profile").value(nullValue()));
  }

  @DisplayName("/api/users - 사용자명 누락으로 유효성 검증에 실패한다.")
  @Test
  void createUser_withBlankUsername_shouldReturnBadRequest() throws Exception {
    // given
    UserCreateDTO invalidRequest = new UserCreateDTO("", "test@abc.com", "pass1234");

    MockMultipartFile createPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(invalidRequest)
    );

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(createPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(jsonPath("$.details.errors").isArray());
  }

  @DisplayName("/api/users/{userId} - 사용자 정보를 프로필과 함께 수정한다.")
  @Test
  void updateUser_withProfileImage() throws Exception {
    // given
    UUID userId = UUID.randomUUID();

    //요청 데이터
    UserUpdateDTO request = new UserUpdateDTO("updatedUser", "updated@abc.com", "newPassword");

    //응답 데이터
    BinaryContentDto profileDto = new BinaryContentDto(
        UUID.randomUUID(),
        "new-profile.png",
        2048L,
        "image/png"
    );

    UserDto response = new UserDto(
        userId,
        "updatedUser",
        "updated@abc.com",
        profileDto,
        true
    );

    // JSON part
    MockMultipartFile updateDtoPart = new MockMultipartFile(
        "userUpdateDTO",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    // 이미지 파일 part
    MockMultipartFile profile = new MockMultipartFile(
        "profile",
        "new-profile.png",
        "image/png",
        "fake-image-content".getBytes()
    );

    given(userService.update(eq(userId), any(), any())).willReturn(response);

    // when & then
    mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{userId}", userId)
            .file(updateDtoPart)
            .file(profile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("updatedUser"))
        .andExpect(jsonPath("$.email").value("updated@abc.com"))
        .andExpect(jsonPath("$.profile.id").value(profileDto.getId().toString()))
        .andExpect(jsonPath("$.profile.fileName").value("new-profile.png"))
        .andExpect(jsonPath("$.profile.size").value(2048))
        .andExpect(jsonPath("$.profile.contentType").value("image/png"));
  }

  @DisplayName("/api/users/{userId} - 사용자명 누락으로 유효성 검증에 실패한다.")
  @Test
  void updateUser_withBlankUsername() throws Exception {
    // given
    UUID userId = UUID.randomUUID();

    // 사용자명 비어 있음
    UserUpdateDTO invalidDto = new UserUpdateDTO("", "test@abc.com", "pass1234");

    MockMultipartFile updatePart = new MockMultipartFile(
        "userUpdateDTO",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(invalidDto)
    );

    // when & then
    mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{userId}", userId)
            .file(updatePart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(jsonPath("$.details.errors").isArray());
  }

  @DisplayName("/api/users/{userId} - 사용자를 삭제한다.")
  @Test
  void deleteUser() throws Exception {
    // given
    UUID userId = UUID.randomUUID();

    // userService.delete()는 반환값이 없으므로 mocking만 해줌
    doNothing().when(userService).delete(userId);

    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());
  }
}