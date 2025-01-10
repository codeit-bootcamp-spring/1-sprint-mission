package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFUser implements UserService {
    private final List<User> users = new ArrayList<>();

    @Override
    public User createUser(String username) {
        User newUser = new User(username);
        users.add(newUser);
        return newUser;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public Optional<User> getUser(UUID uuid) {
        for (User user : users) {
            if (user.getId().equals(uuid)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> updateUser(UUID uuid, String username) {
        for (User user : users) {
            if (user.getId().equals(uuid)) {
                user.updateUsername(username);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> deleteUser(UUID uuid) {
        for (User user : users) {
            if (user.getId().equals(uuid)) {
                users.remove(user);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
