package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Map;
import java.util.UUID;

public interface UserStatusRepository {
    void save(UserStatus userStatus);
    UserStatus findById(UUID id);
    Map<UUID, UserStatus> ffindAll();
    void delete(UUID id);
}