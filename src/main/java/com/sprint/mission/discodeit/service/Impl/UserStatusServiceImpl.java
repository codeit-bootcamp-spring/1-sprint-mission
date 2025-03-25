package com.sprint.mission.discodeit.service.Impl;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

  private final UserStatusRepository userStatusRepository;

  @Transactional
  @Override
  public UserStatusDto create(UserStatusDto userStatusDTO) {
    UserStatus userStatus = new UserStatus(Instant.now());
    userStatusRepository.save(userStatus);
    return new UserStatusDto(userStatus.getId(), userStatus.getLastSeen());
  }

  @Override
  public UserStatusDto find(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new RestApiException(DomainErrorCode.USER_STATUS_NOT_FOUND,
            "UserStatus not found"));
    return new UserStatusDto(userStatus.getId(), userStatus.getLastSeen());
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    userStatusRepository.deleteByUserId(userId);
  }

  @Override
  public UserStatusType getUserOnlineStatus(UUID userId) {
    // 수정: UUID 대신 userId 변수 사용
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new RestApiException(DomainErrorCode.USER_STATUS_NOT_FOUND,
            "UserStatus not found"));
    return onOffStatus(userStatus.getLastSeen());
  }

  @Transactional
  public UserStatusType onOffStatus(Instant lastSeen) {
    Instant now = Instant.now();
    Instant offline = lastSeen.plus(5, ChronoUnit.MINUTES);
    return now.isBefore(offline) ? UserStatusType.ONLINE : UserStatusType.OFFLINE;
  }
}