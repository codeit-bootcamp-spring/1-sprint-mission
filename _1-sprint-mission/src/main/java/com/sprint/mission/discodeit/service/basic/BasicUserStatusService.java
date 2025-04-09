package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundExeption;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusAlreadyExistException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatusDto create(UserStatusCreateRequest userStatusCreateRequest) {
    UUID userId = userStatusCreateRequest.userId();

    //User 존재하지 않으면 예외 발생
    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> new UserNotFoundExeption(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)));

    Optional.ofNullable(user.getStatus())
        .ifPresent(status -> {
          throw new UserStatusAlreadyExistException(ErrorCode.USERSTATUS_ALREADY_EXIST,
              Map.of("userstatus", user.getStatus()));
        });

    UserStatus userStatus = new UserStatus(user,
        userStatusCreateRequest.lastActiveAt());
    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto findById(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(
            () -> new UserStatusNotFoundException(ErrorCode.USERSTATUS_NOT_FOUND,
                Map.of("userstatusId", userStatusId)));
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }


  @Transactional
  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new UserStatusNotFoundException(ErrorCode.USERSTATUS_NOT_FOUND,
                Map.of("userstatusId", userStatusId)));

    userStatus.updateLastActiveAt(userStatusUpdateRequest.newLastActiveAt());

    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);

  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId,
      UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new UserStatusNotFoundException(ErrorCode.USERSTATUS_NOT_FOUND,
                Map.of("userId", userId)));
    userStatus.updateLastActiveAt(userStatusUpdateRequest.newLastActiveAt());

    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      throw new UserStatusNotFoundException(ErrorCode.USERSTATUS_NOT_FOUND,
          Map.of("userstatusId", userStatusId));
    }
    userStatusRepository.deleteById(userStatusId);
  }

}
