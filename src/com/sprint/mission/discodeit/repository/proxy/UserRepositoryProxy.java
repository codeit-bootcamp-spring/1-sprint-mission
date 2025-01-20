package com.sprint.mission.discodeit.repository.proxy;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.log.ServiceLogger;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.UUID;

public class UserRepositoryProxy implements UserRepository {
    private final ServiceLogger logger;
    private final UserRepository userRepository;

    public UserRepositoryProxy(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger = ServiceLogger.getInstance();
    }

    @Override
    public User createUser(User userInfoToCreate) {
        User   creation   = userRepository.createUser(userInfoToCreate);
        String logMessage = "User creation failed";
        UUID   userId     = userInfoToCreate.getId();

        if (creation == User.EMPTY_USER) {
            logger.warning(logMessage, userId);
        }

        return creation;
    }

    @Override
    public User findUserById(UUID key) {
        User find = userRepository.findUserById(key);

        if (find == User.EMPTY_USER) {
            logger.warning("User find failed", key);
        }

        return find;
    }

    @Override
    public User updateUserById(UUID key, User userInfoToUpdate) {
        User   update     = userRepository.updateUserById(key, userInfoToUpdate);
        String logMessage = "User update failed";

        if (update == User.EMPTY_USER) {
            logger.warning(logMessage, key);
        }

        return update;
    }

    @Override
    public User deleteUserById(UUID key) {
        User deletion = userRepository.deleteUserById(key);

        if (deletion == User.EMPTY_USER) {
            logger.warning("User deletion failed", key);
        }

        return deletion;
    }
}
