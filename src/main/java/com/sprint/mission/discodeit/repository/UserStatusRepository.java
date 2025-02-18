package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
  UserStatus save(UserStatus paramUserStatus);
  Optional<UserStatus> findById(UUID paramUUID);
  List<UserStatus> findAll();
  UserStatus updateByUserId(UUID paramUUID, UserStatus paramUserStatus);
  void deleteById(UUID paramUUID);
}
