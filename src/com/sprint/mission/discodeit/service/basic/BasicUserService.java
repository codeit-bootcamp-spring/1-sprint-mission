package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class BasicUserService {

    public static User setupUser(UserService userService, User userToCreate) {
        System.out.println("---------------------------------");
        System.out.println("userService.createUser()");

        User user = userService.createUser(userToCreate);

        System.out.println("pass User '"+ user.getName() + "'! ");

        if (user == User.EMPTY_USER) {
            System.out.println("already exist!");
        }
        System.out.println("User info: " + user);
        System.out.println();

        return user;
    }

    public static User updateUser(UserService userService, User userToUpdate) {
        return userService.updateUserById(userToUpdate.getId(), userToUpdate);
    }

    public static User searchUser(UserService userService, UUID key) {
        System.out.println("---------------------------------");
        System.out.println("userService.findUserById()");


        return userService.findUserById(key);
    }

    public static User removeUser(UserService userService, UUID key) {
        return userService.deleteUserById(key);
    }
}
