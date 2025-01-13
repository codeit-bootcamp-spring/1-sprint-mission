package com.sprint.mission.discodeit.service.proxy;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class UserServiceProxy implements UserService {
    private final UserService userService;

    public UserServiceProxy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User createUser(User userToCreate) {
        return userService.createUser(userToCreate);
    }

    @Override
    public User findUserById(UUID key) {
        return userService.findUserById(key);
    }

    @Override
    public User updateUserById(UUID key, User userToUpdate) {
        return userService.updateUserById(key, userToUpdate);
    }

    @Override
    public User deleteUserById(UUID key) {
        return userService.deleteUserById(key);
    }
}
