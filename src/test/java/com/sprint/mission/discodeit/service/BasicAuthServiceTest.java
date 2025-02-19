package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.login.LoginRequest;
import com.sprint.mission.discodeit.dto.login.LoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BasicAuthService basicAuthService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testUser", "test@example.com", "password123");
    }

    @Test
    @DisplayName("로그인 기능 확인")
    void login_Success() {
        // Given
        LoginRequest request = new LoginRequest("testUser", "password123");

        when(userRepository.findByUsername(request.getUsername()))
                .thenReturn(Optional.of(testUser));

        // When
        LoginResponse response = basicAuthService.login(request);

        // Then
        assertNotNull(response);
        assertEquals(testUser.getUsername(), response.getUsername());
        verify(userRepository, times(1)).findByUsername(request.getUsername());
    }

    @Test
    @DisplayName("로그인 실패 이름 중복")
    void login_Fail_InvalidUsername() {
        // Given
        LoginRequest request = new LoginRequest("wrongUser", "password123");

        when(userRepository.findByUsername(request.getUsername()))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicAuthService.login(request));
        verify(userRepository, times(1)).findByUsername(request.getUsername());
    }

    @Test
    @DisplayName("로그인 실패 비밀번호 중복")
    void login_Fail_InvalidPassword() {
        // Given
        LoginRequest request = new LoginRequest("testUser", "wrongPassword");

        when(userRepository.findByUsername(request.getUsername()))
                .thenReturn(Optional.of(testUser));

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicAuthService.login(request));
        verify(userRepository, times(1)).findByUsername(request.getUsername());
    }
}
