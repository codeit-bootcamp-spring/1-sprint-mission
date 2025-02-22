package com.sprint.mission.service;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.LoginRequest;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class BasicAuthService implements com.sprint.mission.service.AuthService {

  private final JCFUserRepository userRepository;

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
