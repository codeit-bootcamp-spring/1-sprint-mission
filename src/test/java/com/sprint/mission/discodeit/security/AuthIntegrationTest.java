package com.sprint.mission.discodeit.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter.LoginRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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

//    @Test
//    @WithMockUser(roles = {"ADMIN"})
//    void 관리자권한으로_권한변경_API_접근_성공() throws Exception {
//        UserRoleUpdateRequest request = new UserRoleUpdateRequest(
//            UUID.randomUUID(), Role.ROLE_ADMIN
//        );
//
//        mockMvc.perform(put("/api/auth/role")
//                .with(csrf())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isOk());
//    }


}
