package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(User user) {
        if(userRepository.findById(user.getUserId()) != null){
            System.out.println("User with ID " + user.getUserId() + " already exists.");
            return;
        }
        userRepository.save(user);
        System.out.println("User created: " + user.getUserName() + " (ID: " + user.getUserId() + ")");

    }

    @Override
    public User readUser(String userId) {
        return userRepository.findById(userId);
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
        userRepository.delete(userId);
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.delete(userId);
    }
}
