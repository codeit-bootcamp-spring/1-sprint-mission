package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.collection.Users;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFUser implements UserService {
    private final Users users = new Users();

    @Override
    public User createUser(String username) {
        User newUser = new User(username);
        users.add(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public Map<UUID, User> getUsers() {
        return users.asReadOnly();
    }

    @Override
    public Optional<User> getUser(UUID id) {
        return users.get(id);
    }

    @Override
    public Optional<User> updateUser(UUID id, String newUsername) {
        return users.update(id, newUsername);
    }

    @Override
    public Optional<User> deleteUser(UUID id) {
        return users.remove(id);
    }
}
