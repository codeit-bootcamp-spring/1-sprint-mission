package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Map;
import java.util.UUID;

public interface UserStatusRepository {
    // 저장
    void save(UserStatus userStatus);

    // 불러오기
    Map<UUID, UserStatus> load();

    // 삭제
    void delete(UUID id);
}
