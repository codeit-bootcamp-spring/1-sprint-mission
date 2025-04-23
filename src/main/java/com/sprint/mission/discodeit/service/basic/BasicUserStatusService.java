package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  @Transactional(readOnly = true)
  public UserStatusDto findById(String userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(UUID.fromString(userStatusId))
        .orElseThrow(() -> new UserStatusNotFoundException(ErrorCode.USER_STATUS_NOT_FOUND));

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public UserStatusDto findByUserId(String userId) {
    User user = userRepository.findById(UUID.fromString(userId))
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    UserStatus userStatus = userStatusRepository.findByUser(user)
        .orElseThrow(() -> new UserStatusNotFoundException(ErrorCode.USER_STATUS_NOT_FOUND));

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream().map(userStatusMapper::toDto).toList();
  }

  @Override
  @Transactional
  public UserStatusDto create(CreateUserStatusDto createUserStatusDto)
      throws DiscodeitException {
    User user = userRepository.findById(createUserStatusDto.getUserId())
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    UserStatus userStatus = userStatusRepository.findByUser(user).orElse(null);
    if (userStatus != null) {
      throw new UserStatusException(ErrorCode.INVALID_USER_STATUS);
      //todo 에러코드 수정
    }
    userStatus = new UserStatus(user);

    userStatusRepository.save(userStatus);

    return userStatusMapper.toDto(userStatus);

  }

  @Override
  @Transactional
  public UserStatusDto updateByUserId(String id, UserStatusUpdateRequest userStatusUpdateRequest) {

    User user = userRepository.findById(UUID.fromString(id))
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    UserStatus userStatus = user.getStatus();
    userStatus.setUpdatedAt(userStatusUpdateRequest.newLastActiveAt());
    userStatusRepository.save(userStatus);

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional
  public boolean delete(String userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(UUID.fromString(userStatusId))
        .orElseThrow(() -> new UserStatusNotFoundException(ErrorCode.USER_STATUS_NOT_FOUND));

    userStatusRepository.delete(userStatus);

    return true;
  }
}
