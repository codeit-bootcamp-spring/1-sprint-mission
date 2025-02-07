package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    public User createUser(String name, String email, String password) {
        if (userValidator.isValidName(name) && userValidator.isValidEmail(email)) {
            User newUser = User.createUser(name, email, password);
            userRepository.save(newUser);
            log.info("Create User: {}", newUser);
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
                .orElseThrow(() -> new NoSuchElementException("User does not exist"));
    }

    @Override
    public void updateUser(UUID id, String newName, String newEmail, String newPassword) {
        User user = searchById(id);
        if (userValidator.isValidName(newName) && userValidator.isValidEmail(newEmail)) {
            user.update(newName, newEmail, newPassword);
            userRepository.save(user);
            log.info("Update User :{}", user);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
