package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user_status.CreateUserStatusDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserStatusService userStatusService;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public UserResponseDto login(String username, String password) {

    User targetUser = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

    if(!PasswordEncryptor.checkPassword(password, targetUser.getPassword())){
      throw new UserValidationException();
    }

    UserStatus userStatus = userStatusRepository.findByUserId(targetUser.getUUID()).orElseGet(() -> userStatusService.create(new UserStatus(targetUser.getUUID(),Instant.now())));

    userStatus.updateLastOnline();

    userStatusRepository.save(userStatus);

    BinaryContent content = targetUser.getProfileImage();

    return UserResponseDto.from(targetUser, userStatus, content);
  }
}
