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
    public void createUser(User user) {
        userRepository.createUser(user);
    }

    @Override
    public User createUser(String userName, String email, String password) {
        User user = new User(UUID.randomUUID(), System.currentTimeMillis(), System.currentTimeMillis(), userName, email, password);
        userRepository.createUser(user);
        return user;
    }

    @Override
    public User getUser(UUID id) {
        return userRepository.getUser(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void updateUser(UUID id, String userName) {
        User existingUser = userRepository.getUser(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다: " + id));
        existingUser.update(userName);
        userRepository.updateUser(id, userName);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.getUser(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다: " + id));
        userRepository.deleteUser(id);
    }
}
