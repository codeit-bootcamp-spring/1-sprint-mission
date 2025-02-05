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
    public User create(String username, String email, String password) {
        try {
            User newUser = new User(username, email, password);
            if(userRepository.existsByEmail(email) == true){
                throw new IllegalArgumentException("User already exists");
            }
            userRepository.save(newUser);
            System.out.println("User created: " + username + " (email: " + email + ")");
            return newUser;
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create user: " + e.getMessage());
            return null;
        }
    }

    @Override
    public User find(UUID userId) {
        try {
            return userRepository.findById(userId)
                    .orElseThrow(()-> new IllegalArgumentException("User not found"));
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to read user: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                throw new IllegalStateException("No users found in the system.");
            }
            return users;
        } catch (IllegalStateException e) {
            System.out.println("Failed to read all users: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        try {
            User user = find(userId);
            if(userRepository.existsByEmail(newEmail) == true){
                throw new IllegalArgumentException("Email already exists");
            }
            if (userRepository.existsByPassword(newPassword) == true){
                throw new IllegalArgumentException("Password already exists");
            }
            user.update(newUsername, newEmail, newPassword);
            System.out.println("User updated: " + newUsername + " (Email: " + newEmail + ")");
            userRepository.save(user);
            return user;
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to update user: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(UUID userId) {
        try {
            if(userRepository.existsById(userId) == false){
                throw new IllegalArgumentException("User not found");
            }
            System.out.println("User deleted: " + find(userId).getEmail());
            userRepository.deleteById(userId);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to delete user: " + e.getMessage());
        }
    }

}
