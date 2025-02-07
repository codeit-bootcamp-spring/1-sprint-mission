package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BasicAuthServiceTest {

    @InjectMocks
    private BasicAuthService authService;

    @Mock
    private UserRepository userRepository; // UserRepository를 Mock 객체로 생성

    @Mock
    private UserStatusRepository userStatusRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
    }

    @Test
    void login_withValidCredentials_returnsUserDto() {
        // Given
        String username = "testUser";
        String password = "testPassword";
        User user = new User(username, "test@example.com", "123456789", password); // 테스트용 User 객체 생성
        AuthLoginRequest request = new AuthLoginRequest(username, password); // 로그인 요청 객체

        // When
        UserDto result = authService.login(request); // 로그인 메서드 실행

        // Then
        assertNotNull(result); // 결과가 null이 아님을 확인
        assertEquals(user.getId(), result.userId()); // UserDto의 ID가 올바른지 확인
        assertEquals(user.getUsername(), result.username()); // UserDto의 username이 올바른지 확인
        assertEquals(user.getEmail(), result.email()); // UserDto의 email이 올바른지 확인
        assertEquals(user.getPhoneNumber(), result.phoneNumber()); // UserDto의 phoneNumber가 올바른지 확인
        assertTrue(result.isOnline()); // isAdmin 값이 false인지 확인
    }

    @Test
    void login_withInvalidPassword_throwsException() {
        // Given
        String username = "testUser";
        String password = "wrongPassword";
        User user = new User(username, "test@example.com", "123456789", "correctPassword"); // 비밀번호가 다를 테스트용 User 객체 생성
        AuthLoginRequest request = new AuthLoginRequest(username, password); // 로그인 요청 객체


        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(request); // 잘못된 비밀번호로 로그인 시도
        });

        assertEquals("올바르지 않은 아이디나 비밀번호입니다.", exception.getMessage()); // 예외 메시지가 정확한지 확인
    }

    @Test
    void login_withNonExistingUser_throwsException() {
        // Given
        String username = "nonExistentUser";
        String password = "testPassword";
        AuthLoginRequest request = new AuthLoginRequest(username, password); // 로그인 요청 객체

        // Mock behavior
        when(userRepository.findByUsername(username)).thenReturn(null); // 해당 유저가 없을 때

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(request); // 존재하지 않는 유저로 로그인 시도
        });

        assertEquals("올바르지 않은 아이디나 비밀번호입니다.", exception.getMessage()); // 예외 메시지가 정확한지 확인
    }
}
