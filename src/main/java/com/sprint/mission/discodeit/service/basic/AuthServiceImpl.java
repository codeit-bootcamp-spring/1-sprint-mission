package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.LoginResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.sprint.mission.discodeit.constant.ErrorConstant.PASSWORD_MATCH_ERROR;
import static com.sprint.mission.discodeit.constant.UserConstant.NO_MATCHING_USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserStatusService userStatusService;
  private final UserMapper userMapper;

  @Override
  public LoginResponseDto login(String username, String password) {

    log.info("[Login Request] : 요청 수신");
    User targetUser = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(NO_MATCHING_USER));

    if(!PasswordEncryptor.checkPassword(password, targetUser.getPassword())){
      log.info("[Login Request] : 비밀번호 검증 실패");
      throw new UserValidationException(PASSWORD_MATCH_ERROR);
    }

    UserStatus userStatus = userStatusRepository.findByUserId(targetUser.getId()).orElseGet(() -> userStatusService.create(new UserStatus(targetUser.getId(),Instant.now())));
    log.info("[Login Request] : 사용자 상태 성공");

    userStatus.updateLastOnline(Instant.now());

    userStatusRepository.save(userStatus);

    return userMapper.toLoginResponse(targetUser);
  }
}
