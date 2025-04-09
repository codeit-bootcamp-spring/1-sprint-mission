package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserResponseDto login(String username, String password) {

    User targetUser = userRepository.findByUsernameWithProfileAndStatus(username)
        .orElseThrow(() -> {
              log.debug("[LOGIN FAILED] : [USER WITH USERNAME {} NOT FOUND]", username);
              return new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("username", username));
            }

        );

    if (!PasswordEncryptor.checkPassword(password, targetUser.getPassword())) {
      log.debug("[LOGIN FAILED] : [PASSWORD DOES NOT MATCH FOR USER: {}]", username);
      throw new UserException(ErrorCode.PASSWORD_MATCH_ERROR, Map.of("username", username));
    }

    targetUser.getStatus().updateLastOnline(Instant.now());
    userRepository.save(targetUser);

    log.debug("[LOGIN SUCCESS] [USERNAME : {}]", username);

    return userMapper.toDto(targetUser);

  }
}
