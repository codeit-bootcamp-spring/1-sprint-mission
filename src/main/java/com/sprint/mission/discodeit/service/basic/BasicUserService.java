package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    public User createUser(String name, String email, String password) {
        if (userValidator.isValidName(name) && userValidator.isValidEmail(email)) {
            User newUser = User.createUser(name, email, password);
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
    public User searchById(UUID id) {
        return userRepository.findById(id) // Optional 을 받아서 처리
                .orElseThrow(() -> new NoSuchElementException("user does not exist"));
    }

    @Override
    public void updateUser(UUID id, String newName, String newEmail, String newPassword) {
        User user = searchById(id);
        if (userValidator.isValidName(newName) && userValidator.isValidEmail(newEmail)) {
            user.update(newName, newEmail, newPassword);
            userRepository.save(user);
            System.out.println("success updateUser");
        }
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
