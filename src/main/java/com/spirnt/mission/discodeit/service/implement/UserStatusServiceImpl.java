package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusDto;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.mapper.UserStatusMapper;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.repository.UserStatusRepository;
import com.spirnt.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

  private final UserStatusMapper userStatusMapper;

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatusDto create(UserStatusCreateRequest userStatusCreateRequest) {
    UUID userId = userStatusCreateRequest.userId();
    // User가 존재하지 않으면 예외 발생
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id: " + userId + " not found"));
    // 이미 User와 관련된 객체가 존재하면 예외 발생
    if (userStatusRepository.existsByUserId(userId)) {
      throw new IllegalArgumentException("The UserStatus with UserId" + userId + "Already Exists");
    }
    UserStatus userStatus = userStatusRepository.save(new UserStatus(user,
        userStatusCreateRequest.type(), Instant.now()));
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto find(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest,
      Instant serverTime) {
    userStatusRepository.updateById(userStatusId, userStatusUpdateRequest.newLastActiveAt());
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with id:" + userStatusId + " not found"));
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto updateByUserId(UUID userId,
      UserStatusUpdateRequest userStatusUpdateRequest) {
    // User가 존재하지 않으면 예외 발생
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("UserStatus with userId" + userId + " not found");
    }
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
    userStatusRepository.updateById(userStatus.getId(), userStatusUpdateRequest.newLastActiveAt());
    userStatus.update(userStatusUpdateRequest.newLastActiveAt());
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public void delete(UUID userStatusId) {
    userStatusRepository.deleteById(userStatusId);
  }

}
