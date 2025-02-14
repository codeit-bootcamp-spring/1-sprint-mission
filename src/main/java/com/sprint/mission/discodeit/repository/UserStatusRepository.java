package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.List;

public interface UserStatusRepository {

    UserStatus findById(String id);

    List<UserStatus> findAll();

    UserStatus save(UserStatus userStatus);

    boolean delete(String id);

}
