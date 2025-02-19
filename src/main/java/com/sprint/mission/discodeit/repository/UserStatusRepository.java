package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    void save(UserStatus userStatus);
    Optional<UserStatus> findById(UUID id);
    Map<UUID, UserStatus> findAll();
    void delete(UUID id);
}