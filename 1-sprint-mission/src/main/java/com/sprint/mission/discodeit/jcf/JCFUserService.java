package com.sprint.mission.discodeit.jcf;

import project.entity.User;
import project.service.UserService;


import java.util.ArrayList;
import java.util.List;

public class JCFUserService implements UserService {
    private static JCFUserService instance;
    private final List<User> userList;

    public JCFUserService() {
        this.userList = new ArrayList<>();
    }

    public static synchronized JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }
        return instance;
    }


    @Override
    public void createUser(User user) {
        for (User existingUser : userList) {
            if (existingUser.getUserId().equals(user.getUserId())) {
                System.out.println("User with ID " + user.getUserId() + " already exists.");
                return;
            }
        }
        userList.add(user);
        System.out.println("User created: " + user.getUserName() + " (ID: " + user.getUserId() + ")");
    }

    @Override
    public User readUser(String userId) {
        return userList.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> readAllUsers() {
        return new ArrayList<>(userList);
    }

    @Override
    public void updateUser(String userId, String newUserName) {
        User user = readUser(userId);
        if (user != null) {
            user.updateUserName(newUserName);
            System.out.println("User updated: " + user.getUserName() + " (ID: " + user.getUserId() + ")");
        } else {
            System.out.println("User with ID " + userId + " not found.");
        }
    }

    @Override
    public void deleteUser(String userId) {
        User user = readUser(userId);
        if (user != null) {
            userList.remove(user);
            System.out.println("User with ID " + userId + " deleted.");
        } else {
            System.out.println("User with ID " + userId + " not found.");
        }
    }
}
