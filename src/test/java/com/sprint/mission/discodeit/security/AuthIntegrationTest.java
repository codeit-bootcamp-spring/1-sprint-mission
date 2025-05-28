package com.sprint.mission.discodeit.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter.LoginRequest;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/schema-test.sql")
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final String username = "test";
    private final String password = "testtest";
    private User user;

    @BeforeEach
    void setUp() {
        user = User.createUser(
            username,
            "tset@test.test",
            passwordEncoder.encode(password),
            null);

        user = userRepository.saveAndFlush(user);
        UserStatus userStatus = userStatusRepository.save(UserStatus.createUserStatus(user));
        user.updateStatus(userStatus);
    }

    @Test
    void 로그인_성공_응답은_userResponse_반환() throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void 로그인_실패는_401과_ErrorResponse_반환() throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, "비밀번호");

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("로그인에 실패하였습니다."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void 관리자_권한으로_권한변경_성공() throws Exception {
        UserRoleUpdateRequest request = new UserRoleUpdateRequest(
            user.getId(), Role.CHANNEL_MANAGER
        );

        mockMvc.perform(put("/api/auth/role")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.role").value("CHANNEL_MANAGER"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void 일반_유저_권한으로_권한변경_실패() throws Exception {
        UserRoleUpdateRequest request = new UserRoleUpdateRequest(
            user.getId(), Role.CHANNEL_MANAGER
        );

        mockMvc.perform(put("/api/auth/role")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    void 로그인_시_rememberMe_쿠키_발급() throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .param("remember-me", "true")
            )
            .andExpect(status().isOk())
            .andExpect(cookie().exists("remember-me"))
            .andDo(print());
    }

    @Test
    void rememberMe_쿠키만으로_접속() throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .param("remember-me", "true")
            )
            .andReturn();

        String rememberMeCookie = loginResult.getResponse().getCookie("remember-me").getValue();

        mockMvc.perform(get("/api/auth/me")
                .cookie(new Cookie("remember-me", rememberMeCookie)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(username))
            .andDo(print());

    }


}
