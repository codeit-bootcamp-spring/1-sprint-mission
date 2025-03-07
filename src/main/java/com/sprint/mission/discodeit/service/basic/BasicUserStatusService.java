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
    UserStatus userStatus = userStatusRepository.findById(userStatusId);
    if (userStatus == null) {
      throw new IllegalArgumentException("userStatus not found");
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
    User user = userRepository.findById(createUserStatusDto.userId());
    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    if (userStatusRepository.findByUserId(createUserStatusDto.userId()) != null) {
      throw new IllegalArgumentException("userStatus already exists");
    }
    UserStatus userStatus = new UserStatus();

    return UserStatusDto.from(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatusDto updateByUserId(String id, UpdateUserStatusDto updateUserStatusDto) {

    if (userRepository.findById(id) == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    UserStatus userStatus = userStatusRepository.findById(id);
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
    return userStatusRepository.delete(userStatusId);
  }
}
