package com.sprint.mission.service;

import com.sprint.mission.entity.UserStatus;
import com.sprint.mission.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(User user);
    List<UserStatus> findAll() ;
    UserStatus updateByUserId(UUID userId);
    void delete(UUID statusId);
}
