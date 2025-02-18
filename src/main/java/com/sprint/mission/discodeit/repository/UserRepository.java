package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.UUID;

public interface UserRepository {
    // 저장
    void save(User user);

    // 불러오기
    Map<UUID, User> load();

    // 삭제
    void delete(UUID id);
}
