package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@Import({UserService.class, UserStatusService.class})
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserStatusService userStatusService;

  @Nested
  class CreateUserTest {

    @Test
    void createUser_success() throws Exception {
      // Given
      String username = "name";
      String email = "a@a.com";
      String password = "password";
      CreateUserRequest request = new CreateUserRequest(username, email, password);
      UUID id = UUID.randomUUID();
      UserDto userDto = new UserDto(id, username, email, null, false);

      MockMultipartFile userCreateRequestPart = new MockMultipartFile(
          "userCreateRequest",
          "",
          MediaType.APPLICATION_JSON_VALUE,
          new ObjectMapper().writeValueAsBytes(request)
      );

      MockMultipartFile profileImagePart = new MockMultipartFile(
          "profile",
          "profile.jpg",
          MediaType.MULTIPART_FORM_DATA_VALUE,
          "dummy image content".getBytes()
      );

      given(userService.createUser(any(CreateUserRequest.class), any(MultipartFile.class)))
          .willReturn(userDto);

      // When & Then
      mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
              .file(userCreateRequestPart)
              .file(profileImagePart)
              .contentType(MediaType.MULTIPART_FORM_DATA)
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value(id.toString()))
          .andExpect(jsonPath("$.username").value(username))
          .andExpect(jsonPath("$.email").value(email));
    }
  }

}
