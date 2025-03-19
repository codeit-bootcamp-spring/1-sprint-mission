package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public User login(LoginRequest loginRequest) {
    String username = loginRequest.username();
    String password = loginRequest.password();

    // 지연 로딩을 고려한 엔티티 조회
    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new NoSuchElementException("User with username " + username + " not found"));

    // 변경 감지를 위한 검증 로직
    if (!user.getPassword().equals(password)) {
      throw new IllegalArgumentException("Wrong password");
    }

    return user;
  }
}