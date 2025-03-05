package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    // 처음 활동 상태 저장
    UserStatus save(UserStatus userStatus);

    Optional<UserStatus> findById(UUID userStatusId);

    Optional<UserStatus> findByUserId(UUID userId);

    List<UserStatus> findAll();

    List<UserStatus> findAllByUserId(UUID userId);

    boolean existsById(UUID userStatusId);

    void deleteById(UUID userStatusId);

    void deleteByUserId(UUID userId);
}

