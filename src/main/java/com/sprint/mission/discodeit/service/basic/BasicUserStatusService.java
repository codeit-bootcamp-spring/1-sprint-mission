package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
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
  public UserStatusResponseDto findById(String userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId);
    if (userStatus == null) {
      throw new IllegalArgumentException("userStatus not found");
    }

    return UserStatusResponseDto.from(userStatus);
  }

  @Override
  public List<UserStatusResponseDto> findAll() {
    return userStatusRepository.findAll().stream().map(UserStatusResponseDto::from).toList();
  }

  @Override
  public UserStatusResponseDto create(CreateUserStatusDto createUserStatusDto)
      throws CustomException {
    if (userRepository.findById(createUserStatusDto.userId()) == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    if (userStatusRepository.findById(createUserStatusDto.userId()) != null) {
      throw new IllegalArgumentException("userStatus already exists");
    }
    UserStatus userStatus = new UserStatus(createUserStatusDto.userId());

    return UserStatusResponseDto.from(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatusResponseDto updateByUserId(String id, UpdateUserStatusDto updateUserStatusDto) {

    if (userRepository.findById(id) == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    UserStatus userStatus = userStatusRepository.findById(id);
    if (userStatus == null) {
      throw new IllegalArgumentException("userStatus not found");
    }
    if (userStatus.isUpdated(updateUserStatusDto.updateAt())) {
      return UserStatusResponseDto.from(userStatusRepository.save(userStatus));
    }
    return UserStatusResponseDto.from(userStatus);
  }

  @Override
  public boolean delete(String userStatusId) {
    return userStatusRepository.delete(userStatusId);
  }
}
