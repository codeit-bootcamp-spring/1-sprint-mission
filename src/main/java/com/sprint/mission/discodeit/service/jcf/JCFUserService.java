package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(String password, String name) {
        User user = new User(password, name);
        userRepository.save(user);
    }

    @Override
    public User findUserById(UUID id) {
        return userRepository.findUserById(id)
            .orElseThrow(() -> new IllegalArgumentException("user not found: " + id));
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findUserByName(name)
            .orElseThrow(() -> new IllegalArgumentException("user not found: " + name));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    public void updateUserName(UUID id, String newName) {
        userRepository.findUserById(id)
            .ifPresentOrElse(u -> u.updateName(newName), () -> {
                throw new IllegalArgumentException("user not found: " + id);
            });
    }

    @Override
    public void updateUserPassword(UUID id, String newPassword) {
        userRepository.findUserById(id)
            .ifPresentOrElse(u -> u.updatePassword(newPassword), () -> {
                throw new IllegalArgumentException("user not found: " + id);
            });
    }

    @Override
    public void removeUser(UUID id) {
        userRepository.remove(id);
    }
}
