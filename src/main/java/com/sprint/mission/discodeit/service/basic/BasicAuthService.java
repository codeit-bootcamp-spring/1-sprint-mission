package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;

  @Override
  public UserResponse getUserById(UUID userId) {
    User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    return UserMapper.INSTANCE.userToUserResponse(user);
  }

  @Override
  public UserResponse updateRole(RoleUpdateRequest request) {
    User user = userRepository.findById(request.userId()).orElseThrow(UserNotFoundException::new);
    user.updateRole(request.newRole());
    return UserMapper.INSTANCE.userToUserResponse(user);
  }

  @Transactional
  @Override
  public void initAdmin() {
    List<User> admins  = userRepository.findAllByRole(Role.ADMIN);
    userRepository.deleteAll(admins);
  }
}
