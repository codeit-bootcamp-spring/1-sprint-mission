package com.sprint.mission.discodeit.service.basic.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class BasicJCFUserService implements UserService {
    private final UserRepository userRepository;

    public BasicJCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .isPresent(()-> new IllegalArgumentException("User ID already exists: " + user.getId()));
        return userRepository.save(existingUser);
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
