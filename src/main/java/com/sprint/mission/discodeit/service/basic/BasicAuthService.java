package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitDetails;
import com.sprint.mission.discodeit.security.RoleUpdateRequest;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final SessionRegistry sessionRegistry;

  @Transactional
  @Override
  public UserDto updateRole(RoleUpdateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> UserNotFoundException.withId(request.userId()));
    user.updateRole(request.newRole());

    sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> principal instanceof DiscodeitDetails)
        .map(DiscodeitDetails.class::cast)
        .filter(details -> details.getUser().getId().equals(request.userId()))
        .findFirst()
        .ifPresent(details -> {
          List<SessionInformation> activeSessions = sessionRegistry.getAllSessions(details, false);
          activeSessions.forEach(SessionInformation::expireNow);
        });

    return userMapper.toDto(user);
  }

}
