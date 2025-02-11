package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(String userName, String email, String password) {
        User user = new User(userName, email, password);
        return userRepository.save(user);
    }

    @Override
    public User find(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
        user.update(newUsername, newEmail);
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsId(userId)) {
            throw new NoSuchElementException("해당 유저가 없습니다.");
        }
        userRepository.delete(userId);
    }
}
