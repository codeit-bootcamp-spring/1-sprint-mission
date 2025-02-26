package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserLoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  public User login(UserLoginRequest userLoginRequest) {

    User existUser = userRepository.getAllUsers().stream()
        .filter(user -> user.getUsername().equals(userLoginRequest.username()))
        .findAny()
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다.")); // 전역 404

    if (!userLoginRequest.password().equals(existUser.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다."); // 전역 400
    }
    /* 스프린트 미션 5 심화 조건 중 API 스펙을 준수
    UserLoginResponse userLoginResponse = new UserLoginResponse(loginUser.getUsername(),
        loginUser.getEmail());
    */

    return existUser;
  }
}
