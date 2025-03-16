package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.BadRequestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.repository.jpa.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserStatusValidator {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  public void validateUserStatus(UUID userId) {
    validateUserId(userId);
    checkDuplicateUserStatus(userId);
  }

  public void validateUserId(UUID userId) {
    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
  }

  private void checkDuplicateUserStatus(UUID userId) {
    if (userStatusRepository.findByUser_Id(userId).isPresent()) {
      throw new BadRequestException(ErrorCode.USER_STATUS_DUPLICATE);
    }

  }

}
