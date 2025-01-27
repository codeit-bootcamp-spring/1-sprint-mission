package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User ID already exists: " + user.getId());
        }
        User existUser = userRepository.save(user);
        return existUser;
    }

    @Override
    public Optional<User> readUser(UUID existUserId) {
        return userRepository.findById(existUserId);
    }

    @Override
    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(UUID existUserId, User updateUser) {
        //isPresent를 통해 orElseThrow를 만들려고 했는데 잘 됐는지 잘 모르겠다.
        User existUser = userRepository.findById(existUserId)
                .orElseThrow(() -> new IllegalArgumentException("User ID does not exist: " + existUserId));

        existUser.updateTime();
        existUser.updateUserid(updateUser.getUserid());
        existUser.updateUsername(updateUser.getUsername());
        existUser.updatePassword(updateUser.getPassword());
        existUser.updateUserEmail(updateUser.getEmail());

        return userRepository.save(existUser);
    }

    @Override
    public boolean deleteUser(UUID userId) {
        User existUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User ID does not exist: " + userId));

        userRepository.delete(existUser.getId());
        return true;
    }
}
