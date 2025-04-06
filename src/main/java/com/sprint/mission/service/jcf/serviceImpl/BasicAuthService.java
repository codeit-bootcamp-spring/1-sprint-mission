package com.sprint.mission.service.jcf.serviceImpl;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.LoginRequest;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  @Override
  public User login(LoginRequest loginRequest) {
    String username = loginRequest.username();
    String password = loginRequest.password();

    User loginUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER_MATCHING_NAME));

    if (!loginUser.getPassword().equals(password)) {
      throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
    }

    return loginUser;
  }
}
