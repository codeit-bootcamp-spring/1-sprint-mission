package com.sprint.misson.discordeit.service;

import com.sprint.misson.discordeit.entity.status.UserStatus;

import java.util.List;

public interface UserStatusService {

    UserStatus findById(String userStatusId);

    List<UserStatus> findAll();

    boolean update(UserStatus userStatus);

    boolean updateByUserId(String userStatusId, UserStatus userStatus);

    boolean delete(String userStatusId);
}
