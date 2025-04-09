package com.sprint.mission.discodeit.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import({GlobalExceptionHandler.class})
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserStatusService userStatusService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testCreateUser() throws Exception {
    // given
    String username = "username";
    String email = "email";
    String password = "password";
    UserCreateRequest request = new UserCreateRequest(username, email, password);

    MockMultipartFile userCreateRequestPart = new MockMultipartFile(
        "userCreateRequest",                      // @RequestPart 이름
        "",                                       // 파일 이름은 비워도 됨
        MediaType.APPLICATION_JSON_VALUE,         // content type
        objectMapper.writeValueAsBytes(request)   // JSON 바이트
    );

    MockMultipartFile profileImage = new MockMultipartFile(
        "profile", "profile.png",                 // @RequestPart 이름 + 파일명
        MediaType.IMAGE_PNG_VALUE,                // content type
        "image-bytes".getBytes()                  // 임의의 이미지 바이트
    );

    UserDto createdUser = new UserDto(UUID.randomUUID(), username, email, null, false);

    when(userService.create(any(UserCreateRequest.class), any(Optional.class)))
        .thenReturn(createdUser);

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequestPart)
            .file(profileImage)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.email").value(email))
        .andExpect(jsonPath("$.username").value(username));
  }
}
