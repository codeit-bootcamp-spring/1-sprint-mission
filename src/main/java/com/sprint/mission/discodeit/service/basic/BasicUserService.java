package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;


import java.util.List;
import java.util.Optional;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String name, String phone, String password) {
        if (!User.isValidPassword(password)) {
            throw new ServiceException(ErrorCode.INVALID_PASSWORD);
        }

        if (!User.isValidPhone(phone)) {
            throw new ServiceException(ErrorCode.INVALID_WRITER);
        }

        userRepository.findByPhone(phone).ifPresent(user -> {
                    throw new ServiceException(ErrorCode.DUPLICATE_PHONE);});

        User user = new User(name, phone, password);
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User updateUserPassword(User user, String newPass) {
        existUser(user);
        user.update(newPass);
        userRepository.save(user);
        return user;
    }

    @Override
    public void delete(User removeUser) {
        existUser(removeUser);
        userRepository.delete(removeUser);
    }

    private void existUser(User user) {
        userRepository.findByPhone(user.getPhone())
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USER));
    }
}
