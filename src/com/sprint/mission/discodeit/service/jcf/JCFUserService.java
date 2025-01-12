package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data; // assume that it is repository
    private final UserValidator   userValidator;

    private JCFUserService() {
        data          = new HashMap<>();
        userValidator = UserValidator.getInstance();
    }

    private static final class InstanceHolder {
        private final static JCFUserService INSTANCE = new JCFUserService();
    }

    public static JCFUserService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public User createUser(User userToCreate) {
        userValidator.validateBaseEntityFormat(userToCreate);
        userValidator.validateNameFormat(userToCreate);

        UUID key = userToCreate.getId();
        return Optional.ofNullable(data.putIfAbsent(key, userToCreate))
                .map(existingUser -> User.createEmptyUser())
                .orElse(userToCreate);
    }

    @Override
    public User findUserById(UUID key) {
        return Optional.ofNullable(data.get(key))
                .orElse(User.createEmptyUser());
    }

    @Override
    public User updateUserById(UUID key, User userToUpdate) {
        User exsitingUser = findUserById(key);
        userValidator.validateUserIdEquality(exsitingUser, userToUpdate);
        userValidator.validateBaseEntityFormat(userToUpdate);
        userValidator.validateNameFormat(userToUpdate);

        return Optional.ofNullable(data.computeIfPresent(
                key, (id, user)-> userToUpdate))
                .orElse(User.createEmptyUser());
    }

    @Override
    public User deleteUserById(UUID key) {
        return Optional.ofNullable(data.remove(key))
                .orElse(User.createEmptyUser());
    }
}
