package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        if (userRepository.findAll().stream().anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }
        userRepository.save(user);
    }

    @Override
    public User getUser(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID id, String newUsername, String newPassword) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        user.setUsername(newUsername);
        user.setPassword(newPassword);
        userRepository.update(id, user);
    }

    @Override
    public void deleteUser(UUID id) {
        if (userRepository.findById(id) == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        userRepository.delete(id);
    }
}
