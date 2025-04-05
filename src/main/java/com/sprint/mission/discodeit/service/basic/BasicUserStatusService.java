package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserStatusRequest;
import com.sprint.mission.discodeit.dto.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.global.exception.userstatus.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserStatusMapper userStatusMapper;
  private final UserRepository userRepository;

  @Override
  public UserStatusResponse create(UUID userId) {

    User user = findUserByUserIdOrThrow(userId);
    if (userStatusRepository.existsByUserId(userId)) {
      throw new UserStatusAlreadyExistsException(ErrorCode.USER_STATUS_IS_ALREADY_EXIST,
          Map.of("userId", userId));
    }

    UserStatus newUserStatus = UserStatus.createUserStatus(user);
    log.info("Created UserStatus - id: {}", newUserStatus.getId());
    return userStatusMapper.entityToDto(userStatusRepository.save(newUserStatus));
  }

  @Override
  public UserStatusResponse findById(UUID id) {
    UserStatus userStatus = findByIdOrThrow(id);
    return userStatusMapper.entityToDto(userStatus);
  }

  @Override
  public UserStatusResponse findByUserId(UUID userId) {
    findUserByUserIdOrThrow(userId);
    UserStatus userStatus = findByUserIdOrThrow(userId);

    return userStatusMapper.entityToDto(userStatus);
  }

  @Override
  public UserStatusResponse updateByUserId(UUID userId, UserStatusRequest.Update request) {
    findUserByUserIdOrThrow(userId);
    UserStatus userStatus = findByUserIdOrThrow(userId);

    userStatus.updateLastActiveAt(request.getNewLastActiveAt());
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
    findByIdOrThrow(id);
    userStatusRepository.deleteById(id);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    findByUserIdOrThrow(userId);
    userStatusRepository.deleteByUserId(userId);
  }

  private UserStatus findByIdOrThrow(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(
            () -> new UserStatusNotFoundException(ErrorCode.USER_STATUS_NOT_FOUND,
                Map.of("id", id)));
  }

  private UserStatus findByUserIdOrThrow(UUID userId) {
    return userStatusRepository.findById(userId)
        .orElseThrow(
            () -> new UserStatusNotFoundException(ErrorCode.USER_STATUS_NOT_FOUND,
                Map.of("userId", userId)));
  }

  private User findUserByUserIdOrThrow(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(
            () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)));
  }
}
