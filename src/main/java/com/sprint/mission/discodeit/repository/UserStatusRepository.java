package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus readStatus);
    Optional<UserStatus> findById(UUID id);
    void deleteById(UUID id);
}
