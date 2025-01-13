package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.security.Encrypt.getEncryptedPassword;

public class JCFUserService implements UserService {
    private final List<User> data = new ArrayList<>();

    @Override
    public void createUser(String password, String name) {
        password = getEncryptedPassword(password);
        User user = new User(password, name);
        data.add(user);
    }

    @Override
    public Optional<User> findUserById(UUID id) {
        Optional<User> user = data.stream()
            .filter(u -> u.getId().equals(id))
            .findFirst();
        return user;
    }

    @Override
    public Optional<User> findUserByName(String name) {
        Optional<User> user = data.stream()
            .filter(u -> u.getName().equals(name))
            .findFirst();
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return data;
    }

    @Override
    public void updateUserName(UUID id, String newName) {
        findUserById(id).ifPresentOrElse(u -> u.updateName(newName), () -> {
            throw new IllegalArgumentException("user not found: " + id);
        });
    }

    @Override
    public void updateUserPassword(UUID id, String newPassword) {
        findUserById(id).ifPresentOrElse(u -> u.updatePassword(newPassword), () -> {
            throw new IllegalArgumentException("user not found: " + id);
        });
    }

    @Override
    public void removeUser(UUID id) {
        findUserById(id).ifPresent(data::remove);
    }
}
