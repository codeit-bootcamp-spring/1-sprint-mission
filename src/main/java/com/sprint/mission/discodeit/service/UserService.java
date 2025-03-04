package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(String username);

    Map<UUID, User> getUsers();

    Optional<User> getUser(UUID uuid);

    Optional<User> updateUser(UUID uuid, String username);

    Optional<User> deleteUser(UUID uuid);
}
