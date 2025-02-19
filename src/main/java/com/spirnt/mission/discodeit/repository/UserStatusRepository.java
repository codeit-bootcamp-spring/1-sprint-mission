package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.UserStatus;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    void save(UserStatus userStatus);

    void delete(UUID id);

    Optional<UserStatus> findById(UUID id);

    Map<UUID, UserStatus> findAll();

    // 존재 검증
    boolean existsById(UUID id);
}
