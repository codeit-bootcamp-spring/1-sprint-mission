package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;

import java.util.*;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public BasicUserService(UserRepository userRepository) {
        this.userValidator = new UserValidator();
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String name, String email, String password) {
        if (userValidator.isValidName(name) && userValidator.isValidEmail(email)) {
            User newUser = new User(name, email, password);
            userRepository.save(newUser);
            System.out.println("create user: " + newUser.getName());
            return newUser;
        }
        return null;
    }

    @Override
    public List<User> getAllUserList() {
        return userRepository.findAll();
    }

    @Override
    public User searchById(UUID userId) {
        return userRepository.findById(userId) // Optional 을 받아서 처리
                .orElseThrow(() -> new NoSuchElementException("user does not exist"));
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.delete(userId);
    }

    @Override
    public void updateUserName(UUID userId, String newName) {
        User user = searchById(userId);
        if (userValidator.isValidName(newName)) {
            user.updateName(newName);
            userRepository.save(user);
            System.out.println("success update");
        }
    }

    @Override
    public void updateUserEmail(UUID userId, String newEmail) {
        User user = searchById(userId);
        if (userValidator.isValidEmail(newEmail)) {
            searchById(userId).updateEmail(newEmail);
            System.out.println("success update");
        }
    }
}
