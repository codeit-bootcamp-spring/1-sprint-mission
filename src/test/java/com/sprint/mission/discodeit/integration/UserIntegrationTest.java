package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.AuthRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mvc;

  @Test
  @Transactional
  void 회원가입_로그인() throws Exception {
    //회원가입
    var request = new UserCreateRequestDto("홍길동", "hong@test.com", "1234");
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

    //회원가입 api
    mvc.perform(multipart("/api/users")
            .file(jsonPart)
            .file(profile)
            .with(req -> {
              req.setMethod("POST");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("홍길동"))
        .andExpect(jsonPath("$.email").value("hong@test.com"));

    var loginRequest = new AuthRequestDto("홍길동", "1234");

    //login
    mvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk());
  }

  @Test
  void 사용자_생성_로그인_수정_조회() throws Exception {
    // 사용자 생성
    var createRequest = new UserCreateRequestDto("홍길동", "hong@test.com", "1234");
    MockMultipartFile jsonPart = new MockMultipartFile(
        "userCreateRequest", "", "application/json",
        objectMapper.writeValueAsBytes(createRequest));
    MockMultipartFile profile = new MockMultipartFile(
        "profile", "profile.png", "image/png", "dummy".getBytes());

    String userId = mvc.perform(multipart("/api/users")
            .file(jsonPart)
            .file(profile)
            .with(req -> {
              req.setMethod("POST");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("홍길동"))
        .andReturn().getResponse().getContentAsString();

    // 로그인
    var loginRequest = new AuthRequestDto("홍길동", "1234");
    mvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("홍길동"));

    // 사용자 정보 수정
    UserUpdateRequestDto updateRequest = new UserUpdateRequestDto("김철수", "kim@test.com", "");
    MockMultipartFile updateJson = new MockMultipartFile("userUpdateRequest", "",
        "application/json",
        objectMapper.writeValueAsBytes(updateRequest));

    mvc.perform(multipart("/api/users/" + extractId(userId))
            .file(updateJson)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("김철수"))
        .andExpect(jsonPath("$.email").value("kim@test.com"));

    // 전체 사용자 목록 조회
    mvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].email").exists());
  }

  private UUID extractId(String jsonResponse) throws Exception {
    return objectMapper.readTree(jsonResponse).get("id").isTextual()
        ? UUID.fromString(objectMapper.readTree(jsonResponse).get("id").asText())
        : null;
  }

  @Test
  @Transactional
  void 회원탈퇴_후_로그인_실패() throws Exception {
    // 회원가입
    var request = new UserCreateRequestDto("홍길동", "bye@test.com", "1234");
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

    String response = mvc.perform(multipart("/api/users")
            .file(jsonPart)
            .file(profile)
            .with(req -> {
              req.setMethod("POST");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    // 가입한 사용자 ID 파싱
    UserDto userDto = objectMapper.readValue(response, UserDto.class);
    UUID userId = userDto.getId();

    // 회원 탈퇴
    mvc.perform(delete("/api/users/" + userId))
        .andExpect(status().isNoContent());

    // 로그인 시도 → 실패해야 함
    var loginRequest = new AuthRequestDto("bye@test.com", "1234");
    mvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("USER_001"))
        .andExpect(jsonPath("$.message").value("존재하지 않는 사용자입니다."));
  }
}
