package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.security.Encryptor;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final Encryptor encryptor;
  @Value("${discodeit.repository.type}")
  private String repositoryType;
  
  @Override
  public FindUserDto login(String userName, String password) {
    User user = userRepository.findByName(userName)
        .orElseThrow(() -> new NoSuchElementException("user not found with name: " + userName));
    
    String encryptedPassword = encryptor.encryptPassword(password, user.getSalt());
    
    if (user.getEnctyptedPassword().equals(encryptedPassword)) {
      UserStatus status = userStatusRepository.findByUserId(user.getId())
          .orElseThrow(() -> new NoSuchElementException("user status not found with user id: " + user.getId()));
      return new FindUserDto(user.getId(),
          user.getName(),
          user.getEmail(),
          user.getProfileImage(),
          status);
    } else {
      throw new IllegalArgumentException("wrong password: " + password);
    }
  }
}
