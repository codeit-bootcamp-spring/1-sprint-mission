package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {

    UserStatus save(UserStatus userStatus);

    Optional<UserStatus> findById(UUID id);
    Optional<UserStatus> findByUserId(UUID userId); // ✅ 특정 유저의 상태 조회
    boolean existsByUserId(UUID userId);
    void deleteByuserId(UUID userId);
    List<UserStatus> findAll();

}
