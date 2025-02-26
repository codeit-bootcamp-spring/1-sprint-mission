package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;

  @Override
  public UserStatus createUserStatus(UserStatusCreateRequest request) {
    UserStatus userStatus = new UserStatus(request.userId(), request.lastConnectAt());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus findUserStatusById(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("UserStatus가 존재하지 않습니다."));
  }

  @Override
  public UserStatus findUserStatusByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new NoSuchElementException("userId가 " + userId + " 인 UserStatus가 존재하지 않습니다."));
  }

  @Override
  public List<UserStatus> findAllUserStatus() {
    return userStatusRepository.findAll();
  }

  @Override
  public UserStatus updateUserStatus(UserStatusUpdateRequest userStatusUpdateRequest) {
    // UserStatus의 id기준으로 바꾸는 것으로 이해
    UserStatus userStatus =
        userStatusRepository.findById(userStatusUpdateRequest.UserStatusId())
            .orElseGet(() -> {
              System.out.println("No " + userStatusUpdateRequest.UserStatusId() + " UserStatus");
              return null;
            });

    userStatus.refreshLastConnectAt(userStatusUpdateRequest.lastConnectTime());
    userStatus.refreshUpdateAt();
    return userStatus;
  }

  @Override
  public UserStatus updateUserStatusByUserId(UUID userId,
      UserStatusUpdateByUserIdRequest userStatusUpdateByUserIdRequest) {
    UserStatus userStatus =
        userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new NoSuchElementException("userStatus가 존재하지 않습니다."));

    userStatus.refreshLastConnectAt(userStatusUpdateByUserIdRequest.lastConnectAt());
    userStatus.refreshUpdateAt();
    return userStatus;
  }

  @Override
  public void deleteUserStatusById(UUID userStatusId) {
    userStatusRepository.deleteById(userStatusId);
  }

  @Override
  public void delteUserStatusByUserId(UUID userId) {
    userStatusRepository.deleteByUserId(userId);
  }
}
