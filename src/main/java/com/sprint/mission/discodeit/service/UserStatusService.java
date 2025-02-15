package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UUID userId);
    UserStatus find(UUID id);
    UserStatus findByUserId(UUID userId);
    UserStatus updateByUserId(UUID userId);
    List<UserStatus> findAll();
    void delete(UUID id);
    void deleteByUserId(UUID userId);
}
