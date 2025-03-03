package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusRepository {
    boolean save(UserStatus userStatus);
    UserStatus findByUserId(UUID id);

}
