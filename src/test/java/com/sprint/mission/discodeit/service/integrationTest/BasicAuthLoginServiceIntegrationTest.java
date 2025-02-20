package com.sprint.mission.discodeit.service.integrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sprint.mission.discodeit.dto.login.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicAuthLoginService;
import com.sprint.mission.discodeit.validation.AuthLoginValidator;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BasicAuthLoginServiceIntegrationTest {

  @Autowired
  private BasicAuthLoginService basicAuthLoginService;

  @Autowired
  private AuthLoginValidator authLoginValidator;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserStatusRepository userStatusRepository;

  @Test
  void login_Success() {
    // given
    User user = new User("이병규", "test@gmail.com", "01011112222", "qwer123!", null);
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
    userStatusRepository.save(userStatus);

    AuthLoginRequest request = new AuthLoginRequest("이병규", "qwer123!");

    // when
    UserDto result = basicAuthLoginService.login(request);

    // then
    assertNotNull(result);
    assertEquals("이병규", result.username());
    assertEquals("test@gmail.com", result.email());
  }

  @Test
  void login_NotExistsUsername() {
    // given
    User user = new User("이병규", "test@gmail.com", "01011112222", "qwer123!", null);
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
    userStatusRepository.save(userStatus);

    AuthLoginRequest request = new AuthLoginRequest("코드잇", "qwer123!");

    // when, then
    assertThrows(ResourceNotFoundException.class, () -> basicAuthLoginService.login(request));
  }

  @Test
  void login_InvalidPassword() {
    // given
    User user = new User("이병규", "test@gmail.com", "01011112222", "qwer123!", null);
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
    userStatusRepository.save(userStatus);

    AuthLoginRequest request = new AuthLoginRequest("이병규", "asdf909!");

    // when, then
    assertThrows(InvalidResourceException.class, () -> basicAuthLoginService.login(request));
  }
}
