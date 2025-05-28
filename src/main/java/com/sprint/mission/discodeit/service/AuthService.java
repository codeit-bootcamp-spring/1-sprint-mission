package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.User.Role;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final SessionRegistry sessionRegistry;

  public UserResponse toUserResponse(DiscodeitUserDetails principal) {
    return userMapper.toDto(principal.getUser());
  }

  @Transactional
  public UserResponse updateUserRole(RoleUpdateRequest roleUpdateRequest) {
    UUID userId = roleUpdateRequest.userId();
    Role newRole = roleUpdateRequest.newRole();
    User user = userRepository.findById(roleUpdateRequest.userId())
        .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

    if (newRole != null) {
      user.updateRole(newRole);
    }

    sessionRegistry.getAllPrincipals().stream()
        .filter(p -> p instanceof DiscodeitUserDetails userDetails &&
            userDetails.getId().equals(userId))
        .flatMap(p -> sessionRegistry.getAllSessions(p, false).stream())
        .forEach(SessionInformation::expireNow);

    return userMapper.toDto(user);
  }

}
