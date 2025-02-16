package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.logger.service.ServiceLogger;
import com.sprint.mission.discodeit.service.UserService;
import java.util.UUID;

public class BasicUserService {

    private static final ServiceLogger logger = ServiceLogger.getInstance();

    public static User setupUser(UserService userService, User userInfoToCreate) {
        printStartInfo("setupUser(UserService, User)");

        User user = userService.registerUser(userInfoToCreate);

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

        User user = userService.searchUserById(key);

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
        logger.info("---------------------------------");
        logger.info(startInfo);
    }

    private static void printArgsAndUserInfo(UUID key, User user, String messageWhenEmpty) {
        logger.info("pass UUID '" + key + "'! ");
        if (user == User.EMPTY_USER) {
            logger.info(messageWhenEmpty);
        }
        logger.info("User info: " + user);
    }
}
