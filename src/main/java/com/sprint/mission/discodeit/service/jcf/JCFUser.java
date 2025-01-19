package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFUser implements UserService {
    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public User createUser(String username) {
        User newUser = new User(username);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public Map<UUID, User> getUsers() {
        return users;
    }

    @Override
    public Optional<User> getUser(UUID uuid) {
        return Optional.ofNullable(users.get(uuid));
    }

    @Override
    public Optional<User> updateUser(UUID uuid, String username) {
        User updatedUser = users.get(uuid);
        if (updatedUser != null) {
            updatedUser.updateUsername(username);
        }
        return Optional.ofNullable(users.get(uuid));
    }

    @Override
    public Optional<User> deleteUser(UUID uuid) {
        User deletedUser = users.remove(uuid);
        return Optional.ofNullable(deletedUser);
    }
}
