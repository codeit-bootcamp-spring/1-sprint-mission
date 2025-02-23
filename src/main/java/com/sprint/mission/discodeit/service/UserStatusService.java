package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusCreateDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {
  void create(UserStatusCreateDto userStatusCreateDto);
  
  UserStatus findById(UUID userStatusId);
  
  boolean isOnline(UUID userId);
  
  List<UserStatus> findAll();
  
  void update(UUID userStatusId);
  
  void updateByUserId(UUID userId);
  
  void remove(UUID userStatusId);
}
