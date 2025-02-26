package com.sprint.mission.discodeit.service.unitTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.login.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.basic.BasicAuthLoginService;
import com.sprint.mission.discodeit.validation.AuthLoginValidator;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicAuthLoginServiceTest {

  @InjectMocks
  BasicAuthLoginService basicAuthLoginService;

  @Mock
  AuthLoginValidator authLoginValidator;

  @Mock
  UserMapper userMapper;

  @Test
  void login_Success() {
    // given
    AuthLoginRequest request = new AuthLoginRequest("이병규", "qwer123!");
    User user = new User("이병규", "test@gmail.com", "01011112222", "qwer123!", null);
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user.getId(), now);
    UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(),
        user.getPhoneNumber(), user.getProfileId(), userStatus.isOnline(), user.getCreatedAt(),
        user.getUpdatedAt());

    // when
    when(authLoginValidator.findUserByUsername(request.username())).thenReturn(user);
    doNothing().when(authLoginValidator).validatePassword(user, request.password());
    when(authLoginValidator.findUserStatusByUserId(user.getId())).thenReturn(userStatus);
    when(userMapper.userEntityToDto(user, userStatus.isOnline())).thenReturn(userDto);

    UserDto result = basicAuthLoginService.login(request);

    // then
    assertNotNull(result);
    verify(authLoginValidator, times(1)).findUserByUsername(request.username());
  }

  @Test
  void login_NotExistsUsername() {
    // given
    AuthLoginRequest request = new AuthLoginRequest("이병규", "qwer123!");

    // when
    when(authLoginValidator.findUserByUsername(request.username()))
        .thenThrow(ResourceNotFoundException.class);

    // then
    assertThrows(ResourceNotFoundException.class, () -> basicAuthLoginService.login(request));
  }

  @Test
  void login_InvalidPassword() {
    // given
    AuthLoginRequest request = new AuthLoginRequest("이병규", "wrongPassword!");
    User user = new User("이병규", "test@gmail.com", "01011112222", "qwer123!", null);

    // when
    when(authLoginValidator.findUserByUsername(request.username())).thenReturn(user);
    doThrow(InvalidResourceException.class).when(authLoginValidator)
        .validatePassword(user, request.password());

    // then
    assertThrows(InvalidResourceException.class, () -> basicAuthLoginService.login(request));
  }
}