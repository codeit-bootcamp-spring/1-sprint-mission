package com.sprint.mission.discodeit.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CsrfTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("CSRF Token 누락 시 403 오류를 응답한다.")
    void csrf_left_throw_error() throws Exception {
        mvc.perform(post("/transfer")
                        .param("to", "evil")
                        .param("amount", "100"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("CSRF Token 포함 시 200 성공 응답.")
    void csrf_add_throw_success() throws Exception {
        mvc.perform(post("/transfer")
                        .with(csrf())
                        .param("to", "bob")
                        .param("amount", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("잘못된 CSRF Token 포함 시 403 오류 응답")
    void invalid_csrf_throw_403() throws Exception {
        mvc.perform(post("/transfer")
                        .param("_csrf", "invalid-token-value")
                        .param("to", "bob")
                        .param("amount", "10"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET 요청은 CSRF 토큰 불필요함")
    void unnecessary_csrf_token_in_GET() throws Exception {
        mvc.perform(get("/account/balance"))
                .andExpect(status().isOk());
    }

}
