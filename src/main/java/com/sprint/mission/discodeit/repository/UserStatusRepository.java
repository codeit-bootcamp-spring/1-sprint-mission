package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

public interface UserStatusRepository {

    UserStatus findByUserId(String userid);
    void save(UserStatus userStatus);
}
