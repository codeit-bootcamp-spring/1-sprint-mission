package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Map;
import java.util.UUID;

public interface UserStatusRepository {
    Long save(UserStatus userStatus);

    UserStatus load(Long id);

    Map.Entry<Long, UserStatus> load(UUID uuid);

    Map<Long, UserStatus> loadAll();

    Long delete(Long id);

    void update(Long id, UserStatus userStatus);

    // 검색 조건 달린 거 (index Id 제외)
    Map.Entry<Long, UserStatus> findUserStatusByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}
