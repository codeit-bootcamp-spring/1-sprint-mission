package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.auth.LoginRequest;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.AuthService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;

  private final UserStatusService userStatusService;

  @Override
  public User login(LoginRequest loginRequest) {
    User user = userRepository.findByName(loginRequest.username())
        .orElseThrow(() -> new NoSuchElementException(
            "User with username " + loginRequest.username() + " not found"));
    if (!user.getPassword().equals(loginRequest.password())) {
      throw new IllegalArgumentException("Wrong password");
    }
    return user;
  }
}
