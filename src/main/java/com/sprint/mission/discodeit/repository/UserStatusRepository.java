package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);

    UserStatus findById(UUID userId);

    List<UserStatus> findAll();

    void delete(UUID userId);
}
