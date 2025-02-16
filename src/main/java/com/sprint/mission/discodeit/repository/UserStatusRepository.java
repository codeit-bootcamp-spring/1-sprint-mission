package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    // 저장
    void save(UserStatus userStatus);

    // 조회
    Optional<UserStatus> findById(UUID id);
    Optional<UserStatus> findByUserId(UUID userId);

    // 삭제
    void deleteById(UUID id);
}
