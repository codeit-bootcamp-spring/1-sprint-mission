package com.sprint.mission.discodeit.repository.proxy;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.logger.repository.RepositoryLogger;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.UUID;

public class UserRepositoryProxy implements UserRepository {
    private final RepositoryLogger logger;
    private final UserRepository   userRepository;

    public UserRepositoryProxy(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger = RepositoryLogger.getInstance();
    }

    @Override
    public User createUser(User userInfoToCreate) {
        User   creation   = userRepository.createUser(userInfoToCreate);
        String logMessage = "User creation failed";
        UUID   userId     = userInfoToCreate.getId();

        if (creation == User.EMPTY_USER) {
            logger.warning(logMessage, userId);
            System.err.println(logMessage + ", ID: " + userId);
        }

        return creation;
    }

    @Override
    public User findUserById(UUID key) {
        User find = userRepository.findUserById(key);
        String logMessage = "User find failed";

        if (find == User.EMPTY_USER) {
            logger.warning(logMessage, key);
            System.err.println(logMessage + ", ID: " + key);
        }

        return find;
    }

    @Override
    public User updateUserById(UUID key, User userInfoToUpdate) {
        User   update     = userRepository.updateUserById(key, userInfoToUpdate);
        String logMessage = "User update failed";

        if (update == User.EMPTY_USER) {
            logger.warning(logMessage, key);
            System.err.println(logMessage + ", ID: " + key);
        }

        return update;
    }

    @Override
    public User deleteUserById(UUID key) {
        User deletion = userRepository.deleteUserById(key);
        String logMessage = "User deletion failed";

        if (deletion == User.EMPTY_USER) {
            logger.warning(logMessage, key);
            System.err.println(logMessage + ", ID: " + key);
        }

        return deletion;
    }
}
