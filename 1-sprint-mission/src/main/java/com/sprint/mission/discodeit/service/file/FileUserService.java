package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {
    private static FileUserService instance;
    private final UserRepository userRepository;

    private FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository; // 파일에서 읽어서 데이터 초기화

    }

    public static synchronized FileUserService getInstance(UserRepository userRepository){
        if(instance == null){
            instance = new FileUserService(userRepository);
        }
        return instance;
    }

    @Override
    public User create(String username, String email, String password) {
        User newUser = new User(username, email, password);
        try {
            userRepository.save(newUser);
            System.out.println("User created: " + username + " (email: " + email + ")");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create user: " + e.getMessage());
        }
        return newUser;
    }

    @Override
    public User find(UUID userId) {
        try {
            return userRepository.findById(userId)
                    .orElseThrow(()-> new IllegalArgumentException("User not found"));
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to Read user: " + e.getMessage());
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
            System.out.println("User deleted: " + find(userId).getEmail());
            userRepository.deleteById(userId);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to delete user: " + e.getMessage());
        }
    }


}




