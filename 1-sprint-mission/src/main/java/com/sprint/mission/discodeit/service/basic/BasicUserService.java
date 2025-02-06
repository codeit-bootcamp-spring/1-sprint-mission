package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(String username, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }

        User newUser = new User(username, email, password);
        userRepository.save(newUser);

        System.out.println("User created: " + username + " (email: " + email + ")");
        return newUser;
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
            if(userRepository.existsByEmail(newEmail)){
                throw new IllegalArgumentException("Email already exists");
            }
            if (userRepository.existsByPassword(newPassword)){
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
            if(!userRepository.existsById(userId)){
                throw new IllegalArgumentException("User not found");
            }
            System.out.println("User deleted: " + find(userId).getEmail());
            userRepository.deleteById(userId);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to delete user: " + e.getMessage());
        }
    }

}
