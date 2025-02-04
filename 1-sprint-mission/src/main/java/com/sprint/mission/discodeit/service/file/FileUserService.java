package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;

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
    public User createUser(User user) {
        try {
            userRepository.save(user);
            System.out.println("User created: " + user.getUserName() + " (ID: " + user.getUserId() + ")");
            return user;
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create user >>>> " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<User> readUser(String userId) {
        try {
            return userRepository.findById(userId);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Search user >>>> " + e.getMessage());
            throw e;
        }

    }

    @Override
    public List<User> readAllUsers() {
        List<User>users = userRepository.findAll();
        if(users.isEmpty()){
            System.out.println("   No users found. ");
        }
        return users;
    }

    @Override
    public void updateUser(String userId, String newUserName) {
        try {
            Optional<User> user = userRepository.findById(userId);
            user.get().setUserName(newUserName);
            userRepository.delete(userId);
            userRepository.save(user.orElse(null));
            System.out.println("User updated: " + user.get().getUserName() + " (ID: " + user.get().getUserId() + ")");
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Update user >>>> " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(String userId) {
        Optional<User> user = userRepository.findById(userId);
        try {
            userRepository.delete(userId);
            System.out.println("User with ID " + userId + " deleted.");

        }catch (IllegalArgumentException e){
            System.out.println("User with ID " + userId + " not found.");
        }

    }
}




