package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class BasicUserService {

    public static User setupUser(UserService userService, User userInfoToCreate) {
        printStartInfo("setupUser(UserService, User)");

        User user = userService.createUser(userInfoToCreate);

        printArgsAndUserInfo(userInfoToCreate.getId(), user, "Already exist!");

        return user;
    }

    public static User updateUser(UserService userService, UUID key, User userInfoToUpdate) {
        printStartInfo("updateUser(UserService, UUID, User)");

        User user = userService.updateUserById(key, userInfoToUpdate);

        printArgsAndUserInfo(key, user, "Not exist!");

        return user;
    }

    public static User searchUser(UserService userService, UUID key) {
        printStartInfo("searchUser(UserService, UUID)");

        User user = userService.findUserById(key);

        printArgsAndUserInfo(key, user, "Not exist!");

        return user;
    }

    public static User removeUser(UserService userService, UUID key) {
        printStartInfo("removeUser(UserService, UUID)");

        User user = userService.deleteUserById(key);

        printArgsAndUserInfo(key, user, "Not exist!");

        return user;
    }

    private static void printStartInfo(String startInfo) {
        System.out.println("---------------------------------");
        System.out.println(startInfo);
    }

    private static void printArgsAndUserInfo(UUID key, User user, String messageWhenEmpty) {
        System.out.println("pass UUID '" + key + "'! ");
        if (user == User.EMPTY_USER) {
            System.out.println(messageWhenEmpty);
        }
        System.out.println("User info: " + user);
        System.out.println();
    }
}
