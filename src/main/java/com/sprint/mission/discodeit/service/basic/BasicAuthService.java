package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserDto login(LoginRequest loginRequest) {
    String username = loginRequest.username();
    String password = loginRequest.password();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new NoSuchElementException("이름이 " + username + "인 회원이 존재하지 않습니다."));

    if (!user.getUsername().equals(username)) {
      throw new NoSuchElementException("해당 이름을 가진 회원이 존재하지 않습니다.");
    }

    if (!user.getPassword().equals(password)) {
      throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
    }

    return toDto(user);
  }

  // TODO private 메서드니까 클래스 안에 있어야 함!
  private UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map((userStatus) -> userStatus.isOnline())
        .orElseThrow(() -> new NoSuchElementException("해당 userStatus가 존재하지 않습니다."));
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


