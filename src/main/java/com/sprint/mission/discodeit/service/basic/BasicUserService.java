package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.util.UserUtil.checkValid;
import static com.sprint.mission.discodeit.util.UserUtil.validUserId;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String username, String email, String phoneNumber, String password) {
        checkValid(username, email, phoneNumber, password);

        User user = new User(username, email, phoneNumber, password);
        return userRepository.save(user);
    }

    @Override
    public User findById(UUID userId) {
        User user = userRepository.findById(userId);
        validUserId(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID userId, String username, String email, String phoneNumber, String password) {
        User checkUser = userRepository.findById(userId);

        validUserId(checkUser);
        checkValid(username, email, phoneNumber, password);

        checkUser.update(username, email, phoneNumber, password);
        return userRepository.save(checkUser);
    }

    @Override
    public void delete(UUID userId) {
        User checkUser = userRepository.findById(userId);

        validUserId(checkUser);
        userRepository.delete(userId);
    }
}
