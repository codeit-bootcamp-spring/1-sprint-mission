package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.persistence.EntityExistsException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  private final UserStatusMapper userStatusMapper;

  @Override
  public UserStatusDto create(UserStatusRequest userStatusRequest) {
    User user = userRepository.findById(userStatusRequest.userId()).orElseThrow(
        () -> new UserNotFoundException(Map.of("User ID: ", userStatusRequest.userId())));

    //해당 User에 대한 UserStatus 객체 존재 검증
    if (userStatusRepository.existsByUserId(userStatusRequest.userId())) {
      throw new EntityExistsException("User:  " + userStatusRequest.userId() + "  가 이미 존재합니다. ");
    }

    UserStatus userStatus = UserStatus.builder()
        .user(user)
        .lastAccessedAt(userStatusRequest.lastAccessedAt())
        .build();

    return userStatusMapper.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public void saveExist(UserStatus userStatus) {
    userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatusDto find(UUID uuid) {
    UserStatus userStatus = userStatusRepository.findById(uuid).orElseThrow(
        () -> new UserNotFoundException(Map.of("User ID: ", uuid)));
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll()
        .stream().map(u -> userStatusMapper.toDto(u)).collect(Collectors.toList());
  }

  private UserStatus findByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId);
  }


  @Override
  public UserStatusDto update(UUID id, UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(
        () -> new NoSuchElementException("UserStatusId: " + id + " 가 없습니다."));
    userStatus.update(userStatusUpdateRequest);
    return userStatusMapper.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatus updateByUserId(UUID userID, UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatus userStatus = userStatusRepository.findByUserId(userID);
    if (userStatus == null) {
      throw new NoSuchElementException("User ID: " + userID + "의 userStatus가 없습니다. ");
    }
    userStatus.update(userStatusUpdateRequest);
    userStatusRepository.save(userStatus);
    return userStatus;
  }

  @Override
  public void delete(UUID uuid) {
    userStatusRepository.deleteById(uuid);
  }
}
