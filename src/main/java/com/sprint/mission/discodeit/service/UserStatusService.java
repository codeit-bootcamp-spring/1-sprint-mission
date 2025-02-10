package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user_status.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;

public interface UserStatusService {
  UserStatus create(CreateUserStatusDto dto);
  UserStatus find(String id);
  UserStatus findByUserId(String userId);
  List<UserStatus> findAll();
  UserStatus update(UpdateUserStatusDto dto);
  UserStatus updateByUserId(String userId, UpdateUserStatusDto dto);
  void delete(String id);
}
