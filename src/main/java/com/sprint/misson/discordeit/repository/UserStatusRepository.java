package com.sprint.misson.discordeit.repository;

import com.sprint.misson.discordeit.entity.status.UserStatus;

import java.util.List;

public interface UserStatusRepository {

    UserStatus findById(String id);

    List<UserStatus> findAll();

    UserStatus save(UserStatus userStatus);

    boolean delete(String id);

}
