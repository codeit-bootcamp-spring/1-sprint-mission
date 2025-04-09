package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

  private final UserStatusRepository userStatusRepository;

  @Override
  @Transactional
  public UserStatusResponseDto updateByUserId(String userId, UpdateUserStatusDto dto) {

    UserStatus status = userStatusRepository.findByUser_Id(UUID.fromString(userId)).orElseThrow(
        () -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND, Map.of("userId", userId))
    );
    Instant updateTime =
        (dto.newLastActivityAt() == null) ? Instant.now() : dto.newLastActivityAt();
    status.updateLastOnline(updateTime);
    userStatusRepository.save(status);
    return new UserStatusResponseDto(
        status.getId().toString(),
        userId,
        updateTime
    );
  }
}
