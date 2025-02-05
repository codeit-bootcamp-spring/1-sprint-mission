package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserResponseDto login(String username, String password) {
    List<User> users = userRepository.findAll();

    User targetUser = users.stream().filter(u -> u.getUsername().equals(username)).findAny().orElseThrow( UserNotFoundException::new);

    if(!PasswordEncryptor.checkPassword(password, targetUser.getPassword())){
      throw new UserValidationException();
    }

    UserStatus userStatus = userStatusRepository.findByUserId(targetUser.getUUID()).orElseGet(() -> createStatusIfNotExists(targetUser.getUUID()));

    return UserResponseDto.from(targetUser, userStatus);
  }

  private UserStatus createStatusIfNotExists(String id) {
    UserStatus newStatus = new UserStatus(id, Instant.now());
    userStatusRepository.save(newStatus);
    return newStatus;
  }
}
