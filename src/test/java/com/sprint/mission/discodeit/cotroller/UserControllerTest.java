package com.sprint.mission.discodeit.cotroller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;


@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc; // 실제 HTTP 요청을 보내는 역할

  @MockBean
  private UserService userService; // Mocking


  @Autowired
  private ObjectMapper objectMapper;


  @BeforeEach
  void setUp() {
  }

  @Test
  @DisplayName("POST /api/users - 성공")
  void createUserApi_Success() throws Exception {

    UserCreateRequest userCreateRequest = new UserCreateRequest(
        "testUsername",
        "test@example.com",
        "testPassword");

    MockMultipartFile mockFile = new MockMultipartFile(
        "binaryContent",
        "profile.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "dummy content".getBytes()
    );

    // JSON 데이터를 위한 multipart 파일 생성
    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        new ObjectMapper().writeValueAsBytes(userCreateRequest)
    );

    UUID userId = UUID.randomUUID();
    BinaryContentDto profileDto = BinaryContentDto.builder()
        .fileName("profile.jpg")
        .id(UUID.randomUUID())
        .size(12L)
        .contentType(MediaType.IMAGE_JPEG_VALUE)
        .build();

    UserDto userDto = UserDto.builder()
        .id(userId)
        .username("testUsername")
        .email("test@example.com")
        .profile(profileDto)
//        .online(true)
        .build();

    given(userService.createUser(any(UserCreateRequest.class), any(Optional.class))).willReturn(
        userDto);

    // multipart 요청 보내기
    mockMvc.perform(multipart("/api/users") // multipart로 요청
            .file(mockFile) // 파일 첨부
            .file(userPart) // 사용자 데이터를 파트로 전송
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)) // Content-Type 설정
        .andExpect(status().isCreated()) // 예상 응답 상태: 201
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("testUsername"))
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.profile.fileName").value("profile.jpg"))
        .andExpect(jsonPath("$.online").value(true));
  }

  @Test
  @DisplayName("POST /api/users - 실패: 유효하지 않은 요청")
  void createUserApi_Fail_InvalidRequest() throws Exception {
    UserCreateRequest invalidRequest =
        new UserCreateRequest(
            "", // 이름 규칙 위반
            "testEmail", // 이메일 규칙 위반
            "testPassword");

    // 유효하지 않은 JSON 데이터를 위한 multipart 파일 생성
    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        new ObjectMapper().writeValueAsBytes(invalidRequest)
    );

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/users - 성공")
  void findAllUsers_Success() throws Exception {
    // Given
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();

    Role role = new Role("ROLE_USER");

    UserDto user1 = new UserDto(
        userId1,
        "user1",
        "user1@example.com",
        null,
        Set.of(role)
    );

    UserDto user2 = new UserDto(
        userId2,
        "user2",
        "user2@example.com",
        null,
        Set.of(role)
    );

    List<UserDto> users = List.of(user1, user2);

    given(userService.showAllUsers()).willReturn(users);

    mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(userId1.toString()))
        .andExpect(jsonPath("$[0].username").value("user1"))
        .andExpect(jsonPath("$[0].online").value(true))
        .andExpect(jsonPath("$[1].id").value(userId2.toString()))
        .andExpect(jsonPath("$[1].username").value("user2"))
        .andExpect(jsonPath("$[1].online").value(false));
  }

  @Test
  @DisplayName("PATCH /api/users/{userId} - 성공")
  void updateUser_Success() throws Exception {

    UserUpdateRequest updateRequest = new UserUpdateRequest(
        "updatedUsername",
        "updated@example.com",
        "UpdatedPassword1!"
    );

    // JSON 데이터를 위한 multipart 파일 생성
    MockMultipartFile userPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        new ObjectMapper().writeValueAsBytes(updateRequest)
    );

    MockMultipartFile mockFile = new MockMultipartFile(
        "binaryContent",
        "updated-profile.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "dummy content".getBytes()
    );

    UUID userId = UUID.randomUUID();
    BinaryContentDto profileDto = BinaryContentDto.builder()
        .fileName("updated-profile.jpg")
        .id(UUID.randomUUID())
        .size(12L)
        .contentType(MediaType.IMAGE_JPEG_VALUE)
        .build();

    UserDto userDto = UserDto.builder()
        .id(userId)
        .username("updatedUsername")
        .email("updated@example.com")
        .profile(profileDto)
//        .online(true)
        .build();

    given(userService.updateUserInfo(eq(userId), any(UserUpdateRequest.class), any(Optional.class)))
        .willReturn(userDto);

    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(userPart)
            .file(mockFile)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("updatedUsername"))
        .andExpect(jsonPath("$.email").value("updated@example.com"))
        .andExpect(jsonPath("$.profile.fileName").value("updated-profile.jpg"))
        .andExpect(jsonPath("$.online").value(true));
  }

  @Test
  @DisplayName("PATCH /api/users/{userId} - 실패 : 존재하지 않는 사용자")
  void updateUser_Fail_UserNotFound() throws Exception {
    // Given
    UUID nonExistentUserId = UUID.randomUUID();
    UserUpdateRequest updateRequest = new UserUpdateRequest(
        "updateduser",
        "updated@example.com",
        "UpdatedPassword1!"
    );

    // JSON 데이터를 위한 multipart 파일 생성
    MockMultipartFile userPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        new ObjectMapper().writeValueAsBytes(updateRequest)
    );

    given(userService.updateUserInfo(
        eq(nonExistentUserId), any(UserUpdateRequest.class), any(Optional.class)))
        .willThrow(new UserNotFoundException(Map.of("nonExistentUserId", nonExistentUserId)));

    mockMvc.perform(multipart("/api/users/{userId}", nonExistentUserId)
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .with(request -> {
              request.setMethod("PATCH"); // 메서드 PATCH로 덮어쓰기
              return request;
            }))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("DELETE /api/users/{userId} - 성공")
  void deleteUser_Success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    willDoNothing().given(userService).removeUserById(userId);

    // When & Then
    mockMvc.perform(delete("/api/users/{userId}", userId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("DELETE /api/users/{userId} - 실패 : 존재하지 않는 사용자")
  void deleteUser_Fail_UserNotFound() throws Exception {
    // Given
    UUID nonExistentUserId = UUID.randomUUID();
    willThrow(new UserNotFoundException(Map.of("nonExistentUserId", nonExistentUserId)))
        .given(userService).removeUserById(nonExistentUserId);

    // When & Then
    mockMvc.perform(delete("/api/users/{userId}", nonExistentUserId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}
