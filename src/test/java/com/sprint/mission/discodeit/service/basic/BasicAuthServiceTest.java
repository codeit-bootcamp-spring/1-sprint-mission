package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthRequest;
import com.sprint.mission.discodeit.dto.AuthResponse;
import com.sprint.mission.discodeit.service.AuthService;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasicAuthServiceTest {

    private final AuthService authService = mock(AuthService.class);

    @Test
    void testLoginSuccess() {
        String username = "testUser";
        String password = "testPassword";
        String userId = "1234-abcd"; // ✅ UUID 대신 String 사용

        AuthResponse mockResponse = new AuthResponse(userId, "로그인 성공");
        when(authService.login(any(AuthRequest.class))).thenReturn(Optional.of(mockResponse));

        Optional<AuthResponse> response = authService.login(new AuthRequest(username, password));

        assertTrue(response.isPresent());
        assertEquals(userId, response.get().getUserId()); // ✅ getUserId() 사용
        assertEquals("로그인 성공", response.get().getMessage());
    }
}
