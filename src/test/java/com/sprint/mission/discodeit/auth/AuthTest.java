package com.sprint.mission.discodeit.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();

    User user = new User("test", "testuser", "test@email.com", passwordEncoder.encode("!@asdf1234"),
        new Phone("010-1111-2222", Phone.RegionCode.KR), Role.USER, "", null);
    userRepository.save(user);
  }

  @Test
  @DisplayName("로그인 성공")
  void login_success() throws Exception {
    JsonNode requestBody = objectMapper.createObjectNode()
        .put("userName", "test")
        .put("password", "!@asdf1234");

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("login success"));
  }

  @Test
  @DisplayName("비밀번호 불일치로 인한 로그인 실패")
  void login_fail_invalid_password() throws Exception {
    JsonNode requestBody = objectMapper.createObjectNode()
        .put("userName", "test")
        .put("password", "wrongPassword1234!");

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody))
            .with(csrf()))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("login failed"));
  }

  @Test
  @DisplayName("사용자명 불일치로 인한 로그인 실패")
  void login_fail_invalid_username() throws Exception {
    JsonNode requestBody = objectMapper.createObjectNode()
        .put("userName", "wronguser")
        .put("password", "!@asdf1234");

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestBody))
            .with(csrf()))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("login failed"));
  }

  @Test
  @DisplayName("현재 로그인한 사용자 정보 조회 성공")
  void me_success() throws Exception {
    // 1. 로그인 요청 → 세션 확보
    MvcResult result = mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test\", \"password\":\"!@asdf1234\"}")
            .with(csrf()))
        .andExpect(status().isOk())
        .andReturn();

    // 2. 로그인 세션 추출
    MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
    assertThat(session).isNotNull();

    // 3. 세션을 포함해 /me 요청
    mockMvc.perform(get("/api/auth/me")
            .session(session)) // 세션 객체 전달
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.nickname").value("testuser"));
  }

  @Test
  @DisplayName("로그아웃 성공 시 세션 무효화 및 Security Context 초기화")
  void logout_success() throws Exception {
    // 로그인
    MvcResult result = mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test\", \"password\":\"!@asdf1234\"}")
            .with(csrf()))
        .andExpect(status().isOk())
        .andReturn();

    MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
    assertThat(session).isNotNull();

    // 로그아웃 요청
    mockMvc.perform(post("/api/auth/logout")
            .with(csrf())
            .session(session))
        .andExpect(status().isOk());

    // 무효화 확인
    mockMvc.perform(get("/api/auth/me")
            .session(session))
        .andExpect(status().isUnauthorized());

  }

}
