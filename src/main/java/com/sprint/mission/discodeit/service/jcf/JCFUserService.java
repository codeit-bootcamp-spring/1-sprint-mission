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
            .orElse(null);
        return user;
    }

    @Override
    public User findUserByName(String name) {
        User user = data.stream()
            .filter(u -> u.getName().equals(name))
            .findFirst()
            .orElse(null);
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return data;
    }

    @Override
    public void updateUserName(UUID id, String newName) {
        data.stream().filter(u -> u.getId().equals(id))
            .findFirst()
            .ifPresentOrElse(user -> user.updateName(newName), () -> System.out.println("user not found"));
    }

    @Override
    public void updateUserPassword(UUID id, String newPassword) {
        data.stream()
            .filter(u -> u.getId().equals(id))
            .findFirst()
            .ifPresentOrElse(user -> user.updatePassword(newPassword), () -> System.out.println("user not found"));
    }

    @Override
    public void removeUser(UUID id) {
        data.stream().filter(u -> u.getId().equals(id))
            .findFirst()
            .ifPresentOrElse(data::remove, () -> System.out.println("user not found"));
    }
}
