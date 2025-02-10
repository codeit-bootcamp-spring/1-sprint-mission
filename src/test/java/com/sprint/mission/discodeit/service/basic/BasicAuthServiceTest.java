package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.validation.ValidateAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicAuthServiceTest {

    @InjectMocks
    private BasicAuthService basicAuthService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private ValidateAuth validateAuth;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void login_Success() {
        // Given
        AuthLoginRequest loginRequest = new AuthLoginRequest("testUsername", "qwer123!");
        User user = new User(loginRequest.username(), "test@gmail.com", "01012345678", loginRequest.password());
        UserStatus userStatus = new UserStatus(user.getUserId(), Instant.now());

        // When
        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(userStatusRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(userStatus));

        UserDto result = basicAuthService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals(result.userId(), user.getUserId());
        assertEquals(result.username(), user.getUsername());
        assertEquals(result.email(), user.getEmail());
        assertEquals(result.phoneNumber(), user.getPhoneNumber());
        assertTrue(result.isOnline());
        assertEquals(result.createdAt(), user.getCreatedAt());
        assertEquals(result.updatedAt(), user.getUpdatedAt());

        verify(userStatusRepository).save(any(UserStatus.class));
    }

    @Test
    public void login_UserNotFound(){
        // given
        AuthLoginRequest loginRequest = new AuthLoginRequest("testUsername", "qwer123!");

        // when
        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicAuthService.login(loginRequest));
    }

    @Test
    public void login_UserStatusNotFound(){
        // given
        AuthLoginRequest loginRequest = new AuthLoginRequest("testUsername", "qwer123!");
        User user = new User(loginRequest.username(), "test@gmail.com", "01012345678", loginRequest.password());

        // when
        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(userStatusRepository.findByUserId(user.getUserId())).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicAuthService.login(loginRequest));
    }
}
