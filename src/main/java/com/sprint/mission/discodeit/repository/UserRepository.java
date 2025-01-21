package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;
import java.util.HashMap;

public interface UserRepository {

    void save(User user);

    User findById(UUID uuid);

    HashMap<UUID, User> findAll();

    void delete(UUID uuid);
}
