package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class BasicAuthService implements AuthService {
  @Qualifier("file")
  private final UserRepository userRepository;
  @Qualifier("file")
  private final UserStatusRepository userStatusRepository;
  
  @Override
  public FindUserDto login(String userName, String password) {
    User user = userRepository.findByName(userName)
        .orElseThrow(() -> new NoSuchElementException("user not found with name: " + userName));
    if (user.getPassword().equals(password)) {
      UserStatus status = userStatusRepository.findByUserId(user.getId())
          .orElseThrow(() -> new NoSuchElementException("user status not found with user id: " + user.getId()));
      return new FindUserDto(user.getId(),
          user.getName(),
          user.getEmail(),
          user.getProfileImage(),
          status);
    }
    throw new NoSuchElementException("wrong password: " + password);
  }
}
