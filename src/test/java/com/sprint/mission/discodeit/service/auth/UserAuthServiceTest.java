package com.sprint.mission.discodeit.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.entity.role.Role;
import com.sprint.mission.discodeit.entity.role.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.status.UserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

  @Mock
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserSessionService userSessionService;

  @Mock
  private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

  @Mock
  private HttpServletRequest request;

  @InjectMocks
  private UserAuthService userAuthService;

  @Test
  @DisplayName("사용자 역할 변경 - 사용자가 없음")
  void failChangeRoleCauseUserNotFound() {
    UUID userId = UUID.randomUUID();

    when(userRepository.findById(userId))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> userAuthService.updateRole(new RoleUpdateRequest(userId,
        Role.ROLE_ADMIN), request))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @DisplayName("사용자 역할 변경 - 성공(오프라인인 경우 세선 삭제 안함)")
  void changeRole() throws IOException {
    UUID userId = UUID.randomUUID();
    User user1 = new User("test1", "test1@mail.com", "{noop}password1234", null, Role.ROLE_USER);

    when(userRepository.findById(userId))
        .thenReturn(Optional.of(user1));
    when(userSessionService.isOnline(user1.getUsername()))
        .thenReturn(false);

    userAuthService.updateRole(new RoleUpdateRequest(userId, Role.ROLE_ADMIN), null);

    assertThat(Role.ROLE_ADMIN).isEqualTo(user1.getRole());

    verify(sessionRepository, never())
        .findByIndexNameAndIndexValue(anyString(), anyString());
    verify(sessionRepository, never())
        .deleteById(anyString());
  }

  @Test
  @DisplayName("사용자 역할 변경 - 성공(온라인인 경우 세션까지 삭제)")
  void failChangeRole() throws IOException {
    UUID userId = UUID.randomUUID();
    User user1 = new User("test1", "test1@mail.com", "{noop}password1234", null, Role.ROLE_USER);

    when(userRepository.findById(userId))
        .thenReturn(Optional.of(user1));
    when(userSessionService.isOnline(user1.getUsername()))
        .thenReturn(true);

    // 세션 테스트
    Map<String, Session> sessionMap = new HashMap<>();
    sessionMap.put("session1", mock(Session.class));

    doReturn(sessionMap)
        .when(sessionRepository)
        .findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,
            user1.getUsername());

    userAuthService.updateRole(new RoleUpdateRequest(userId, Role.ROLE_ADMIN), null);

    assertThat(Role.ROLE_ADMIN).isEqualTo(user1.getRole());

    verify(sessionRepository)
        .findByIndexNameAndIndexValue(anyString(), anyString());
    verify(sessionRepository)
        .deleteById("session1");
  }

}