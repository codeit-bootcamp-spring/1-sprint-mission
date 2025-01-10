package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> data = new ArrayList<>();

    @Override
    public void createUser(String password, String name) {
        User user = new User(password, name);
        data.add(user);
    }

    @Override
    public User findUserById(UUID id) {
        User user = data.stream()
            .filter(u -> u.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("user not found"));
        return user;
    }

    @Override
    public User findUserByName(String name) {
        User user = data.stream()
            .filter(u -> u.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("user not found"));
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return data;
    }

    @Override
    public void updateUserName(UUID id, String newName) {
        User user = findUserById(id);
        user.updateName(newName);
    }

    @Override
    public void updateUserPassword(UUID id, String newPassword) {
        User user = findUserById(id);
        user.updatePassword(newPassword);
    }

    @Override
    public void removeUser(UUID id) {
        data.remove(findUserById(id));
    }
}
