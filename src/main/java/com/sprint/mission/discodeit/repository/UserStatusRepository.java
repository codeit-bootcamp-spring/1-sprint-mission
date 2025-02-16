package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.userstatus.UserStatus;

import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);

    Optional<UserStatus> findByUserId(UUID userId);

    List<UserStatus> findAllOnlineUsers();

    boolean delete(UUID userId);

    List<UserStatus> findAll();

    Optional<UserStatus> findById(UUID id);
}
