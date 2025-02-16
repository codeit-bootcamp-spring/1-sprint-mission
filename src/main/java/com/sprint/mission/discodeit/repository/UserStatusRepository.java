package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    List<UserStatus> findAll();
}
