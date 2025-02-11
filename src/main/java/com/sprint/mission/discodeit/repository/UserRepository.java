package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    User findbyId(UUID uuid);

    Boolean existByUserId(UUID userId);
    boolean existsByUsername(String username);

    Map<UUID, User> load();
    void delete(UUID id);
}
