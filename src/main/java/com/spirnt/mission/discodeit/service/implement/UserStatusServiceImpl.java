package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.repository.UserStatusRepository;
import com.spirnt.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatus create(UserStatusCreateRequest userStatusCreateRequest) {
    // User가 존재하지 않으면 예외 발생
    if (!userRepository.existsById(userStatusCreateRequest.userId())) {
      throw new NoSuchElementException("User ID Not Found");
    }
    // 이미 User와 관련된 객체가 존재하면 예외 발생
    if (userStatusRepository.findAll().values().stream()
        .anyMatch(userStatus -> userStatus.getUserId().equals(userStatusCreateRequest.userId()))) {
      throw new IllegalArgumentException("The ReadStatus with UserId and ChannelId Already Exists");
    }
    UserStatus userStatus = new UserStatus(userStatusCreateRequest.userId(),
        userStatusCreateRequest.type(), Instant.now());
    userStatusRepository.save(userStatus);
    return userStatus;
  }

  @Override
  public UserStatus find(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
    return userStatus;
  }

  @Override
  public List<UserStatus> findAll() {
    Map<UUID, UserStatus> map = userStatusRepository.findAll();
    List<UserStatus> list = map.values().stream()
        .map(userStatus -> this.find(userStatus.getId()))
        .collect(Collectors.toList());
    return list;
  }

  @Override
  public UserStatus update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest,
      Instant serverTime) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
    userStatus.update(userStatusUpdateRequest.newLastActiveAt());
    userStatusRepository.save(userStatus);
    return userStatus;
  }

  @Override
  public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest) {
    // User가 존재하지 않으면 예외 발생
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("UserStatus with userId" + userId + " not found");
    }
    Map<UUID, UserStatus> map = userStatusRepository.findAll();
    UserStatus userStatus = map.values().stream()
        .filter(value -> value.getUserId().equals(userId))
        .findAny()
        .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
    userStatus.update(userStatusUpdateRequest.newLastActiveAt());
    userStatusRepository.save(userStatus);
    return userStatus;
  }

  @Override
  public void delete(UUID userStatusId) {
    userStatusRepository.delete(userStatusId);
  }

  @Override
  public UserStatus findByUserId(UUID userId) {
    List<UserStatus> list = findAll();
    UUID userStatusId = list.stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .map(userStatus -> userStatus.getId())
        .findAny()
        .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
    return find(userStatusId);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    Map<UUID, UserStatus> map = userStatusRepository.findAll();
    UserStatus userStatus = map.values().stream()
        .filter(value -> value.getUserId().equals(userId))
        .findAny()
        .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
    delete(userStatus.getId());
  }
}
