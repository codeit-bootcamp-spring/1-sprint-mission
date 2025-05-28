package com.spirnt.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.spirnt.mission.discodeit.dto.auth.LoginRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.User.InvalidPasswordException;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.basic.BasicAuthService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock

    private UserStatusService userStatusService;

    @InjectMocks
    private BasicAuthService basicAuthService;

    @Test
    @DisplayName("로그인 테스트 - 성공")
    void loginSuccess() {
        // given
        LoginRequest loginRequest = new LoginRequest("alice", "password");
        User user = new User("alice", "alice@mail.com", "password");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(userMapper.toDto(any(User.class))).thenAnswer(invocation -> {
            User userArg = invocation.getArgument(0);
            return new UserDto(userArg.getId(), userArg.getCreatedAt(), userArg.getUpdatedAt(),
                userArg.getUsername(), userArg.getEmail(), true, null);
        });
        // when
        UserDto result = basicAuthService.login(loginRequest);

        // then
        assertNotNull(result);
        assertEquals("alice", result.getUsername());
        assertEquals("alice@mail.com", result.getEmail());
    }

    @Test
    @DisplayName("로그인 테스트 - 실패: 존재하지 않는 유저")
    void loginFailDueToNotFound() {
        // given
        LoginRequest loginRequest = new LoginRequest("alice", "password");
        User user = new User("alice", "alice@mail.com", "password");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());
        // when & then
        assertThrows(UserNotFoundException.class, () -> basicAuthService.login(loginRequest));
    }

    @Test
    @DisplayName("로그인 테스트 - 실패: 잘못된 비밀번호")
    void loginFailDueToWrongPw() {
        // given
        LoginRequest loginRequest = new LoginRequest("alice", "wrong");
        User user = new User("alice", "alice@mail.com", "password");
        ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        // when & then
        assertThrows(InvalidPasswordException.class, () -> basicAuthService.login(loginRequest));
    }

}
