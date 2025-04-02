package com.sprint.mission.discodeit.cotroller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;


@WebMvcTest(UserController.class)
@Import(BasicUserStatusService.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc; // 실제 HTTP 요청을 보내는 역할

  @MockBean
  private UserService userService; // Mocking

  @MockBean
  private UserStatusService userStatusService;


  private UserCreateRequest userCreateRequest;
  private UserDto userDto;

  private UUID userId;

  @BeforeEach
  void setUp() {
    userCreateRequest = new UserCreateRequest("testUsername", "test@example.com", "testPassword");

    userId = UUID.randomUUID();
    userDto = UserDto.builder()
        .id(userId)
        .username("testUsername")
        .email("test@example.com")
        .online(true)
        .build();

    // 서비스 호출시 반환 값 지정
    // given(userService.createUser(any(UserCreateRequest.class), null)).willReturn(userDto); --> Mockito에서 인수 매처(argument matchers)와 원시값(raw value)을 혼합해서 사용하면 안 된다.
    given(
        userService.createUser(any(UserCreateRequest.class),
            any(BinaryContentCreateRequest.class))).willReturn(
        userDto);
  }

  @Test
  @DisplayName("POST /api/users - 성공")
  void createUserApi_Success() throws Exception {
    // 파일 데이터
    MockMultipartFile mockFile = new MockMultipartFile(
        "binaryContent",
        "profile.jpg",
        "image/jpeg",
        "dummy content".getBytes()
    );

    // JSON 데이터를 위한 multipart 파일 생성
    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        "application/json",
        new ObjectMapper().writeValueAsBytes(userCreateRequest)
    );

    // JSON 데이터 직렬화
    String userJson = new ObjectMapper().writeValueAsString(userCreateRequest);

    // multipart 요청 보내기
    mockMvc.perform(MockMvcRequestBuilders
            .multipart("/api/users") // multipart로 요청
            .file(mockFile) // 파일 첨부
            .file(userPart) // 사용자 데이터를 파트로 전송
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)) // Content-Type 설정
        .andExpect(status().isCreated()) // 예상 응답 상태: 201
        .andExpect(jsonPath("$.username").value("testUsername"))
        .andExpect(jsonPath("$.email").value("test@example.com"));
  }

  @Test
  @DisplayName("POST /api/users - 실패: 유저 이름이 입력되지 않았다")
  void createUsrApi_Fail() throws Exception {
    UserCreateRequest invalidRequest = new UserCreateRequest("", "testEmail", "testPassword");

    // 파일 데이터
    MockMultipartFile mockFile = new MockMultipartFile(
        "binaryContent",
        "profile.jpg",
        "image/jpeg",
        "dummy content".getBytes()
    );

    // 유효하지 않은 JSON 데이터를 위한 multipart 파일 생성
    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        "application/json",
        new ObjectMapper().writeValueAsBytes(invalidRequest)
    );

    mockMvc.perform(MockMvcRequestBuilders
            .multipart("/api/users")
            .file(mockFile)
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        )
        .andExpect(status().isBadRequest());
  }

}
