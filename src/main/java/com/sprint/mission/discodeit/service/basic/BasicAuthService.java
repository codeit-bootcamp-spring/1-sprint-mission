package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final SessionRegistry sessionRegistry;

  private final UserMapper userMapper;

  @Transactional
  @Override
  public UserDto updateRole(RoleUpdateRequest roleUpdateRequest) {
    User user = userRepository.findById(roleUpdateRequest.userId())
        .orElseThrow(() -> new UserNotFoundException(
            ErrorCode.USER_NOT_FOUND,
            Map.of("userId", roleUpdateRequest.userId())
        ));

    user.updateRole(roleUpdateRequest.newRole());
    invalidateSessionsOf(user);

    return userMapper.toDto(user);
  }

  public void invalidateSessionsOf(User user) {
    List<Object> principals = sessionRegistry.getAllPrincipals();
    for (Object principal : principals) {
      if (principal instanceof CustomUserDetails customUser) {
        if (customUser.getUser().getId().equals(user.getId())) {
          List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
          for (SessionInformation session : sessions) {
            session.expireNow();
          }
        }
      }
    }
  }
}
