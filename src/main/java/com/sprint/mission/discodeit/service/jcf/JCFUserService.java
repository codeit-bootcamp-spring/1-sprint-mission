package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final List<User> data;

    public JCFUserService() {
        this.data = new ArrayList<>();
    }

    @Override
    public User createUser(UUID id, Long createdAt, Long updatedAt, String name) {
        User user = new User(id, createdAt, updatedAt, name);
        data.add(user);
        return user;
    }

    @Override
    public User getUser(UUID id) {
        return data.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data);
    }

    @Override
    public User updateUser(UUID id, String name, Long updatedAt) {
        User user = getUser(id);
        if (user != null) {
            user.update(name, updatedAt);
            return user;
        }
        return null;
    }


    @Override
    public void deleteUser(UUID id) {
        data.removeIf(user -> user.getId().equals(id));
    }
}
