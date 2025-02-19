package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UUID userId);
    UserStatus findById(UUID id);
    UserStatus findByUserId(UUID userId);
    UserStatus updateByUserId(UUID userId);
    List<UserStatus> findAll();
    void deleteById(UUID id);
    void deleteByUserId(UUID userId);
}
