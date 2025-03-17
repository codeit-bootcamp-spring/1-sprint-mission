package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusRequest;
import com.sprint.mission.discodeit.dto.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sun.jdi.request.DuplicateRequestException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserStatusMapper userStatusMapper;
  private final UserRepository userRepository;

  @Override
  public UserStatusResponse create(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(ErrorCode.USER_NOT_FOUND, "userId : " + userId));
    if (userStatusRepository.existsByUserId(userId)) {
      throw new DuplicateRequestException("UserStatus already exists");
    }
    UserStatus newUserStatus = UserStatus.createUserStatus(user);
    log.info("Create UserStatus: {}", newUserStatus);
    return userStatusMapper.entityToDto(userStatusRepository.save(newUserStatus));
  }

  @Override
  public UserStatusResponse findById(UUID id) {
    UserStatus userStatus = userStatusRepository.findById(id)
        .orElseThrow(() -> new RestApiException(ErrorCode.USER_STATUS_NOT_FOUND, "id : " + id));
    return userStatusMapper.entityToDto(userStatus);
  }

  @Override
  public UserStatusResponse findByUserId(UUID userId) {
    User user = userRepository.findById(userId).orElseThrow(() ->
        new RestApiException(ErrorCode.USER_NOT_FOUND, "id :" + userId));
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new RestApiException(ErrorCode.USER_STATUS_NOT_FOUND, "userId : " + userId));
    return userStatusMapper.entityToDto(userStatus);
  }

  @Override
  public UserStatusResponse updateByUserId(UUID userId, UserStatusRequest.Update request) {
    User user = userRepository.findById(userId).orElseThrow(() ->
        new RestApiException(ErrorCode.USER_NOT_FOUND, "id :" + userId));
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new RestApiException(ErrorCode.USER_STATUS_NOT_FOUND, "userId : " + userId));
    userStatus.updateLastActiveAt(request.newLastActiveAt());
    return userStatusMapper.entityToDto(userStatusRepository.save(userStatus));
  }

  @Override
  public List<UserStatusResponse> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::entityToDto)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(UUID id) {
    userStatusRepository.deleteById(id);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    userStatusRepository.deleteByUserId(userId);
  }
}
