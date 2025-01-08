package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();
    @Override
    public void createUser(String username) {
       User user = new com.sprint.mission.discodeit.entity.User(username);
        data.put(user.getId(), user);
    }

    @Override
    public UUID getUser(UUID id) {
        return data.get(id).getId();
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, String username) {
        User user = data.get(id);
        if (user != null) {
            user.updateUsername(username);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }
}
