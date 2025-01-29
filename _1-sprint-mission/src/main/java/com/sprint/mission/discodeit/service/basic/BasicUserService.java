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
    public User create(String username, String email, String password, String phoneNumber, String address) {
        User user = new User(username, email, password, phoneNumber, address);
        return userRepository.create(user);
    }

    @Override
    public User find(UUID userId) {
        User user = userRepository.findById(userId);
        if (user == null) throw new NoSuchElementException("User not found with ID: " + userId);
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID userId, String field, Object value) {
        User user = find(userId);
        switch (field) {
            case "username" -> user.update((String) value, null, null, null, null);
            case "email" -> user.update(null, (String) value, null, null, null);
            case "password" -> user.update(null, null, (String) value, null, null);
            case "phoneNumber" -> user.update(null, null, null, (String) value, null);
            case "address" -> user.update(null, null, null, null, (String) value);
            default -> throw new IllegalArgumentException("Invalid field: " + field);
        }
        return userRepository.create(user);
    }

    @Override
    public void delete(UUID userId) {
        userRepository.delete(userId);
    }
}
