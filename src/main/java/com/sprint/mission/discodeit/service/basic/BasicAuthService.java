package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.dto.authDto.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserDto login(LoginRequest loginRequest) {
    String username = loginRequest.username();
    String password = loginRequest.password();

    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new ResourceNotFoundException("User not found: " + username));

    if (!user.getPassword().equals(password)) {
      throw new InvalidResourceException("Wrong password");
    }

    boolean isOnline = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElseThrow(() -> new ResourceNotFoundException("User status not found"));
    return new UserDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getUsername(),
        user.getEmail(), user.getProfileId(), isOnline);
  }
}
