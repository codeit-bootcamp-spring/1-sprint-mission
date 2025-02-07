package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.request.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.response.user.UserDTO;
import com.sprint.mission.discodeit.dto.request.user.UserProfileImageDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import com.sprint.mission.discodeit.service.UserService;


import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;
    private static JCFUserService instance;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static synchronized JCFUserService getInstance(UserRepository userRepository) {
        if (instance == null) {
            instance = new JCFUserService(userRepository);
        }
        return instance;
    }

    @Override
    public UserDTO create(UserCreateDTO userCreateDTO, UserProfileImageDTO userProfileImageDTO) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }

        User newUser = new User(username, email, password);
        userRepository.save(newUser);

        System.out.println("User created: " + username + " (email: " + email + ")");
        return newUser;
    }

    @Override
    public UserDTO find(UUID userId) {
        try {
            return userRepository.findById(userId)
                    .orElseThrow(()-> new IllegalArgumentException("User not found"));
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to read user: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<UserDTO> findAll() {
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
    public UserDTO update(UserUpdateDTO userUpdateDTO, UserProfileImageDTO userProfileImageDTO) {
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
