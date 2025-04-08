package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class UserIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserStatusRepository userStatusRepository;

  @Test
  @Transactional
  void 사용자_생성_성공() throws Exception {
    UserCreateRequest request = new UserCreateRequest("yubin", "yubin@codeit.com", "1234");

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    MockMultipartFile profile = new MockMultipartFile(
        "profile",
        "profile.png",
        "image/png",
        "dummy".getBytes()
    );

    mockMvc.perform(multipart("/api/users")
            .file(jsonPart)
            .file(profile)
            .with(req -> {
              req.setMethod("POST");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("yubin"))
        .andExpect(jsonPath("$.email").value("yubin@codeit.com"));

    LoginRequest loginRequest = new LoginRequest("yubin", "1234");

    //login
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void 사용자_수정_성공() throws Exception {
    User user = userRepository.save(new User("yubin", "yubin@old.com", "1234", null));
    UserStatus status = userStatusRepository.save(new UserStatus(user, Instant.now()));
    UUID userId = user.getId();

    UserUpdateRequest updateRequest = new UserUpdateRequest("yubin2", "yubin@new.com", null);

    MockMultipartFile jsonPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(updateRequest)
    );

    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(jsonPart)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("yubin2"))
        .andExpect(jsonPath("$.email").value("yubin@new.com"));
  }

  @Test
  @Transactional
  void 사용자_목록_조회_성공() throws Exception {
    User user1 = new User("yubin", "yubin@test.com", "1234", null);
    User user2 = new User("minji", "minji@test.com", "1234", null);

    userRepository.save(user1);
    userRepository.save(user2);

    UserStatus status1 = new UserStatus(user1, Instant.now());
    UserStatus status2 = new UserStatus(user2, Instant.now());

    mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].username").value("yubin"))
        .andExpect(jsonPath("$[1].username").value("minji"));
  }

  @Test
  @Transactional
  void 사용자_삭제_성공_로그인_실패() throws Exception {
    UserCreateRequest request = new UserCreateRequest("홍길동", "hong@test.com", "1234");
    MockMultipartFile jsonPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    MockMultipartFile profile = new MockMultipartFile(
        "profile",
        "profile.png",
        "image/png",
        "dummy".getBytes()
    );

    String response = mockMvc.perform(multipart("/api/users")
            .file(jsonPart)
            .file(profile)
            .with(req -> {
              req.setMethod("POST");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    UserDto userDto = objectMapper.readValue(response, UserDto.class);
    UUID userId = userDto.id();

    // 회원 삭제
    mockMvc.perform(delete("/api/users/" + userId))
        .andExpect(status().isNoContent());

    // 로그인 시도 - 실패
    LoginRequest loginRequest = new LoginRequest("hong@test.com", "1234");
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("유저를 찾을 수 없습니다."));
  }

}
