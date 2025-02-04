package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(User user) {
        userRepository.save(user);
        System.out.println("User created: " + user.getUserName() + " (ID: " + user.getUserId() + ")");

    }

    @Override
    public User readUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new NoSuchElementException("User with ID " + userId + " not found."));
    }

    @Override
    public List<User> readAllUsers() {
        List<User>users = userRepository.findAll();
        if(users.isEmpty()){
            System.out.println(" No users found. ");
        }
        return users;
    }

    @Override
    public void updateUser(String userId, String newUserName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        user.setUserName(newUserName);
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.delete(userId);
    }
}
