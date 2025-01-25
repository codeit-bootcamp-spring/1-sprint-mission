package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;


import java.util.List;

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
    public void createUser(User user) {
        for (User existingUser : userRepository.findAll()) {
            if (existingUser.getUserId().equals(user.getUserId())) {
                System.out.println("User with ID " + user.getUserId() + " already exists.");
                return;
            }

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
        return userRepository.findAll();
    }

    @Override
    public void updateUser(String userId, String newUserName) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setUserName(newUserName);
            System.out.println("User updated: " + user.getUserName() + " (ID: " + user.getUserId() + ")");
        } else {
            System.out.println("User with ID " + userId + " not found.");
        }
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.delete(userId);
        System.out.println("User deleted : " + userId);

    }
}
