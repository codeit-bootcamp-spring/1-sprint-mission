package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.auth.WrongPasswordException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
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
    String username = loginRequest.getUsername();
    String password = loginRequest.getPassword();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(null));

    if (!user.getUsername().equals(username)) {
      throw new UserNotFoundException(null);
    }

    if (!user.getPassword().equals(password)) {
      throw new WrongPasswordException(null);
    }

    return toDto(user);
  }

  // TODO private 메서드니까 클래스 안에 있어야 함!
  private UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map((userStatus) -> userStatus.isOnline())
        .orElseThrow(() -> new UserStatusNotFoundException(null));
    BinaryContentDto profileDto = new BinaryContentDto(
        user.getProfile().getId(),
        user.getProfile().getFileName(),
        user.getProfile().getSize(),
        user.getProfile().getContentType()
    );

    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        profileDto,
        online
    );
  }
}


