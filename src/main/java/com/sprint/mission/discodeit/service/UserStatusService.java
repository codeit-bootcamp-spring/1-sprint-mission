package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.List;

public interface UserStatusService {

    UserStatus findById(String userStatusId);

    List<UserStatus> findAll();

    UserStatus create(UserStatus userStatus);

    boolean updateByUserId(String userStatusId, UserStatus userStatus);

    boolean delete(String userStatusId);
}
