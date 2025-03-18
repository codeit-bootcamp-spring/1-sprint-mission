package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  public UserStatusDto create(UserStatusCreateRequest userStatusCreateRequest) {
    //User 존재하지 않으면 예외 발생
    if (!userRepository.existsById(userStatusCreateRequest.userId())) {
      throw new NoSuchElementException(
          "User with id " + userStatusCreateRequest.userId() + " does not exist");
    }
    if (userStatusRepository.findByUserId(userStatusCreateRequest.userId()).isPresent()) {
      throw new IllegalArgumentException(
          "UserStatus with id " + userStatusCreateRequest.userId() + " already exists");
    }
    User user = userRepository.findById(userStatusCreateRequest.userId())
        .orElseThrow(() -> new NoSuchElementException(
            "User with id " + userStatusCreateRequest.userId() + " not found"));

    UserStatus userStatus = new UserStatus(user,
        userStatusCreateRequest.lastActiveAt());
    UserStatus saved = userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(saved);
  }

  @Override
  public UserStatusDto findById(UUID userStatusId) {
    UserStatus saved = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
    return userStatusMapper.toDto(saved);
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream().map(userStatusMapper::toDto).toList();
  }


  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));

    userStatus.updateLastActiveAt(userStatus.getLastActiveAt());

    UserStatus saved = userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(saved);

  }

  @Override
  public UserStatusDto updateByUserId(UUID userId,
      UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with userId " + userId + " not found"));
    userStatus.updateLastActiveAt(userStatus.getLastActiveAt());

    UserStatus savedUserStatus = userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(savedUserStatus);
  }

  @Override
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      throw new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
    }
    userStatusRepository.deleteById(userStatusId);
  }

}
