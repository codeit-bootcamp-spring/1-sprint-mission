package com.sprint.mission.discodeit.service.proxy;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.log.service.ServiceLogger;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class UserServiceProxy implements UserService {
    private final ServiceLogger logger;
    private final UserService userService;

    public UserServiceProxy(UserService userService) {
        this.userService = userService;
        logger = ServiceLogger.getInstance();
    }

    @Override
    public User createUser(User userInfoToCreate) {
        User   creation   = User.createEmptyUser();
        String logMessage = "User creation failed";
        UUID   userId     = userInfoToCreate.getId();

        try {
            creation = userService.createUser(userInfoToCreate);
        } catch (InvalidFormatException e) {
            logger.warning(e.getErrorCode(), logMessage, userId);
        }

        if (creation == User.EMPTY_USER) {
            logger.warning(logMessage, userId);
            return creation;
        }

        return creation;
    }

    @Override
    public User findUserById(UUID key) {
        User find = userService.findUserById(key);
        if (find == User.EMPTY_USER) {
            logger.warning("User find failed", key);
            return find;
        }

        return find;
    }

    @Override
    public User updateUserById(UUID key, User userInfoToUpdate) {
        User   update     = User.createEmptyUser();
        String logMessage = "User update failed";

        try {
            update = userService.updateUserById(key, userInfoToUpdate);
        } catch (InvalidFormatException e) {
            logger.warning(e.getErrorCode(), logMessage, key);
        }

        if (update == User.EMPTY_USER) {
            logger.warning(logMessage, key);
            return update;
        }

        return update;
    }

    @Override
    public User deleteUserById(UUID key) {
        User deletion = userService.deleteUserById(key);
        if (deletion == User.EMPTY_USER) {
            logger.warning("User deletion failed", key);
            return deletion;
        }

        return deletion;
    }
}
