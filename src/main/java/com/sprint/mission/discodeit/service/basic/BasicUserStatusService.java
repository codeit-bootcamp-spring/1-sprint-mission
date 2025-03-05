package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validator.UserStatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserStatusValidator userStatusValidator;

  @Override
  public UserStatus create(UserStatusCreateDTO dto) {
    userStatusValidator.validateUserStatus(dto.getUserid());

    UserStatus userStatus = new UserStatus(dto.getUserid());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus find(UUID id) {
    return userStatusRepository.findByUserId(id)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with id " + id + " not found"));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll().stream()
        .peek(UserStatus::isOnline)
        .toList();
  }

  @Override
  public UserStatus update(UUID userStatusId, UserStatusUpdateDTO userStatusUpdateDTO) {
    UserStatus findUserStatus = userStatusRepository.find(userStatusId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_STATUS_NOT_FOUND));
    findUserStatus.updateLastActiveAt(userStatusUpdateDTO.getNewLastActiveAt());
    userStatusRepository.update(findUserStatus);
    return findUserStatus;
  }

  @Override
  public UserStatus updateByUserId(UUID userId, Instant time) {
    UserStatus finduserStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_STATUS_NOT_FOUND));

    finduserStatus.updateLastActiveAt(time);
    userStatusRepository.update(finduserStatus);
    return finduserStatus;
  }

  @Override
  public UUID delete(UUID id) {
    return userStatusRepository.delete(id);
  }

}
