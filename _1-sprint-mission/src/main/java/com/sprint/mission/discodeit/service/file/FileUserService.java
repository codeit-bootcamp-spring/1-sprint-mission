package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private final UserRepository userRepository;

    public FileUserService(File filePath) {
        this.userRepository = new FileUserRepository(filePath);
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
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NoSuchElementException("User not found");
        }

        switch (field.toLowerCase()) {
            case "username":
                user.update((String) value, user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.getAddress());
                break;
            case "email":
                user.update(user.getUsername(), (String) value, user.getPassword(), user.getPhoneNumber(), user.getAddress());
                break;
            case "password":
                user.update(user.getUsername(), user.getEmail(), (String) value, user.getPhoneNumber(), user.getAddress());
                break;
            case "phonenumber":
                user.update(user.getUsername(), user.getEmail(), user.getPassword(), (String) value, user.getAddress());
                break;
            case "address":
                user.update(user.getUsername(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), (String) value);
                break;
            default:
                throw new IllegalArgumentException("Invalid field: " + field);
        }

        return userRepository.create(user);
    }

    @Override
    public void delete(UUID userId) {
        userRepository.delete(userId);
    }
}
