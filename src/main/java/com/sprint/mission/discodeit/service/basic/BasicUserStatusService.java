package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.Interface.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  @Autowired
  private final UserStatusRepository userStatusRepository;
  @Autowired
  private final UserRepository userRepository;


  @Override
  public UserStatus create(UserStatusCreateRequestDto request) {
    if (!userRepository.existsById(request.getUserId())) {
      throw new NoSuchElementException("User not found");
    }
    Optional<UserStatus> existingStatus = userStatusRepository.findByUserId(request.getUserId());
    if (existingStatus.isPresent()) {
      throw new IllegalArgumentException("UserStatus already exists");
    }
    UserStatus userStatus = new UserStatus(request.getUserId(), request.getCreatedAt());
    System.out.println("userStatus 생성:" + userStatus.getId());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus find(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User status not found"));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  public void update(UUID userStatusId, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("UserStatus not found"));
    userStatus.update(request.getNewLastActiveAt());
    userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("UserStatus not found"));
    userStatus.update(request.getNewLastActiveAt());
    return userStatusRepository.save(userStatus);
  }


  @Override
  public void deleteByUserId(UUID userId) {
    if (!userStatusRepository.existsById(userId)) {
      throw new NoSuchElementException("UserStatus not found");
    }
    userStatusRepository.deleteByUserId(userId);
  }
}
