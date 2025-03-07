package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatusDto findById(String userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(UUID.fromString(userStatusId))
        .orElse(null);
    if (userStatus == null) {
      throw new IllegalArgumentException("userStatus not found");
    }

    return UserStatusDto.from(userStatus);
  }

  @Override
  public UserStatusDto findByUserId(String userId) {
    User user = userRepository.findById(UUID.fromString(userId)).orElse(null);
    if (user == null) {
      throw new IllegalArgumentException("user not found");
    }
    UserStatus userStatus = userStatusRepository.findByUser(user).orElse(null);
    if (userStatus == null) {
      throw new IllegalArgumentException("User Status not found");
    }
    return UserStatusDto.from(userStatus);
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream().map(UserStatusDto::from).toList();
  }

  @Override
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

    return UserStatusDto.from(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatusDto updateByUserId(String id, UpdateUserStatusDto updateUserStatusDto) {

    User user = userRepository.findById(UUID.fromString(id)).orElse(null);
    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    UserStatus userStatus = userStatusRepository.findById(UUID.fromString(id)).orElse(null);
    if (userStatus == null) {
      throw new IllegalArgumentException("userStatus not found");
    }
    if (userStatus.isUpdated(updateUserStatusDto.updateAt())) {
      return UserStatusDto.from(userStatusRepository.save(userStatus));
    }
    return UserStatusDto.from(userStatus);
  }

  @Override
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
