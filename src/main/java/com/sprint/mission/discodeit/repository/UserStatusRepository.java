package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {

  UserStatus save(UserStatus userStatus);

  UserStatus find(UUID id);

  List<UserStatus> findAll();

  UserStatus update(UserStatus userStatus);

  Optional<UserStatus> findByUserId(UUID userId);

  UUID delete(UUID id);

  void deleteByUserId(UUID userId);
}
