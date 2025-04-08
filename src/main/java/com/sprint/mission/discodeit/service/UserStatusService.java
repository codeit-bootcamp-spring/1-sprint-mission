package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  @Transactional
  UserStatusDto createUserStatus(UserStatusCreateRequest userStatusCreateRequest);

  UserStatusDto findUserStatusById(UUID userStatusId);

  UserStatusDto findUserStatusByUserId(UUID userId);

  List<UserStatusDto> findAllUserStatus();

  @Transactional
  UserStatusDto updateUserStatus(UserStatusUpdateRequest userStatusUpdateRequest);

  @Transactional
  UserStatusDto updateUserStatusByUserId(UUID userId,
      UserStatusUpdateByUserIdRequest userStatusUpdateByUserIdRequest);

  @Transactional
  void deleteUserStatusById(UUID userStatusId);

  @Transactional
  void delteUserStatusByUserId(UUID userId);
}
