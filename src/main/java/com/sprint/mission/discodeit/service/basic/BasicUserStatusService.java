package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
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
        .orElse(null);
    if (userStatus == null) {
      throw new IllegalArgumentException("userStatus not found");
    }

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public UserStatusDto findByUserId(String userId) {
    User user = userRepository.findById(UUID.fromString(userId)).orElse(null);
    if (user == null) {
      throw new IllegalArgumentException("user not found");
    }
    UserStatus userStatus = userStatusRepository.findByUser(user).orElse(null);
    if (userStatus == null) {
      throw new IllegalArgumentException("User Status not found");
    }
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream().map(userStatusMapper::toDto).toList();
  }

  @Override
  @Transactional
  public UserStatusDto create(CreateUserStatusDto createUserStatusDto)
      throws CustomException {
    User user = userRepository.findById(createUserStatusDto.getUserId()).orElse(null);
    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    UserStatus userStatus = userStatusRepository.findByUser(user).orElse(null);
    if (userStatus != null) {
      throw new IllegalArgumentException("userStatus already exists");
    }
    userStatus = new UserStatus(user);

    userStatusRepository.save(userStatus);

    return userStatusMapper.toDto(userStatus);

  }

  @Override
  @Transactional
  public UserStatusDto updateByUserId(String id, UpdateUserStatusDto updateUserStatusDto) {

    User user = userRepository.findById(UUID.fromString(id)).orElse(null);
    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    UserStatus userStatus = user.getUserStatus();
    userStatus.setUpdatedAt(updateUserStatusDto.updateAt());
    userStatusRepository.save(userStatus);

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional
  public boolean delete(String userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(UUID.fromString(userStatusId))
        .orElse(null);
    if (userStatus == null) {
      throw new IllegalArgumentException("User status not found");
    }
    userStatusRepository.delete(userStatus);

    return true;
  }
}
