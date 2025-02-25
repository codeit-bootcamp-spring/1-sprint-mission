package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
  void save(UserStatus userStatus);
  
  Optional<UserStatus> findById(UUID userStatusId);
  
  Optional<UserStatus> findByUserId(UUID userId);
  
  List<UserStatus> findAll();
  
  boolean isOnline(UUID userId);
  
  void remove(UUID userId);
}
