package com.sprint.mission.discodeit.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.entity.auth.RequestLogin;
import com.sprint.mission.discodeit.entity.auth.ResponseLogin;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.user.UserAuthException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserAuthService userAuthService;

  @Test
  @DisplayName("로그인 시도 - 성공")
  void login() {
    RequestLogin requestLogin = new RequestLogin("userA", "password123");
    User user = new User("userA", "usera@mail.com", "password123", null, null);

    when(userRepository.findUserByUsername(any(String.class)))
        .thenReturn(user);

    ResponseLogin login = userAuthService.login(requestLogin);

    assertThat(login).isNotNull();
    assertThat(login.username()).isEqualTo("userA");
    assertThat(login.email()).isEqualTo("usera@mail.com");

    verify(userRepository).findUserByUsername(any(String.class));
  }

  @Test
  @DisplayName("로그인 시도 - 유저 아이디 검증 실패")
  void loginFail() throws Exception {
    RequestLogin wrongRequestLogin = new RequestLogin("userAA", "password");

    Assertions.assertThrows(UserNotFoundException.class,
        () -> userAuthService.login(wrongRequestLogin));
  }

  @Test
  @DisplayName("로그인 시도 - 유저 비밀번호 검증 실패")
  void loginFailPassword() throws Exception {
    RequestLogin wrongRequestLogin = new RequestLogin("userA", "wrongpassword");
    User user = new User("userA", "usera@mail.com", "password", null, null);

    when(userRepository.findUserByUsername(any(String.class)))
        .thenReturn(user);

    Assertions.assertThrows(UserAuthException.class,
        () -> userAuthService.login(wrongRequestLogin));
  }
}