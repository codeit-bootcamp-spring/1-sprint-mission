package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusRepository {

    UserStatus findByUserId(UUID userid);
    void save(UserStatus userStatus);
}
