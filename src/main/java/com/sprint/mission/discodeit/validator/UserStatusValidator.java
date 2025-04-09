package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.execption.userStatus.UserStatusAlreadyExistException;
import com.sprint.mission.discodeit.execption.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStatusValidator {

  private final UserStatusRepository userStatusRepository;

  public UserStatus validateUserStatusExistsById(UUID userStatusId) {
    return userStatusRepository.findUserStatusById(userStatusId)
        .orElseThrow(() -> new UserStatusNotFoundException(Map.of("userStatusId", userStatusId)));
  }

  public UserStatus validateUserStatusExistsByUser(User user) {
    return userStatusRepository.findUserStatusByUser(user)
        .orElseThrow(() -> new UserStatusNotFoundException(Map.of("userId", user.getId())));
  }

  public void validateDuplicateByUser(User user) {
    if (userStatusRepository.findUserStatusByUser(user).isPresent()) {
      throw new UserStatusAlreadyExistException(Map.of("userId", user.getId()));
    }
  }
}
