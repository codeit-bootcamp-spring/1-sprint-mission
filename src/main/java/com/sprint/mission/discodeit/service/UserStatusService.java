package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserStatusService {
  UserStatus create(UserStatus dto);
  UserStatus find(String id);
  UserStatus findByUserId(String userId);
  List<UserStatus> findAll();
  UserStatusResponseDto updateByUserId(String userId, UpdateUserStatusDto dto);
  Map<String, UserStatus> mapUserToUserStatus(Set<String> users);
  void delete(String id);
  void deleteByUserId(String userId);
}
