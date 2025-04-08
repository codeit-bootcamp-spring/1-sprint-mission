package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserStatusMapper userStatusMapper;

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserStatusDto create(UserStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new UserNotFoundException(
            ErrorCode.USER_NOT_FOUND,
            Map.of("userId", request.userId())
        ));

    userStatusRepository.findAll().forEach(userStatus -> {
      if (userStatus.isSameUserById(request.userId())) {
        throw new UserStatusAlreadyExistsException(
            ErrorCode.USER_STATUS_ALREADY_EXISTS,
            Map.of(
                "userStatusId", userStatus.getId(),
                "userId", request.userId()
            )
        );
      }
    });

    return userStatusMapper.toDto(
        userStatusRepository.save(new UserStatus(user, request.lastActiveAt())));
  }

  @Override
  @Transactional(readOnly = true)
  public UserStatusDto find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(() -> new UserStatusNotFoundException(
            ErrorCode.USER_STATUS_NOT_FOUND,
            Map.of("userStatusId", userStatusId)
        ));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new UserStatusNotFoundException(
            ErrorCode.USER_STATUS_NOT_FOUND,
            Map.of("userStatusId", userStatusId)
        ));
    if (request.newLastActiveAt() != null ){
      userStatus.update(request.newLastActiveAt());
    }

    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserStatusNotFoundException(
            ErrorCode.USER_STATUS_NOT_FOUND,
            Map.of("userId", userId)
        ));
    userStatus.update(request.newLastActiveAt());

    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      throw new UserStatusNotFoundException(
          ErrorCode.USER_STATUS_NOT_FOUND,
          Map.of("userStatusId", userStatusId)
      );
    }

    userStatusRepository.deleteById(userStatusId);
  }
}
