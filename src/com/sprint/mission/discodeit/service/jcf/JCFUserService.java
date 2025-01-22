package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data; // assume that it is repository
    private final UserValidator userValidator;

    public JCFUserService() {
        data = new HashMap<>();
        userValidator = UserValidator.getInstance();
    }

    /**
     * Create the User while ignoring the {@code createAt} and {@code updateAt} fields from {@code userInfoToCreate}
     */
    @Override
    public User createUser(User userInfoToCreate) throws InvalidFormatException {
        validateFormat(userInfoToCreate);

        User userToCreate = new User.Builder(
                userInfoToCreate.getName(), userInfoToCreate.getEmail())
                .id(userInfoToCreate.getId())
                .phoneNumber(userInfoToCreate.getPhoneNumber())
                .build();

        return Optional.ofNullable(data.putIfAbsent(userToCreate.getId(), userToCreate))
                .map(existingUser -> User.createEmptyUser())
                .orElse(userToCreate);
    }

    @Override
    public User findUserById(UUID key) {
        return Optional.ofNullable(data.get(key))
                .orElse(User.createEmptyUser());
    }

    /**
     * Update the User while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields from {@code userInfoToUpdate}
     */
    @Override
    public User updateUserById(UUID key, User userInfoToUpdate) throws InvalidFormatException {
        validateFormat(userInfoToUpdate);

        User exsitingUser = findUserById(key);
        User userToUpdate = new User.Builder(
                userInfoToUpdate.getName(), userInfoToUpdate.getEmail())
                .id(key)
                .createAt(exsitingUser.getCreateAt())
                .phoneNumber(userInfoToUpdate.getPhoneNumber())
                .build();

        return Optional.ofNullable(data.computeIfPresent(
                        key, (id, user) -> userToUpdate))
                .orElse(User.createEmptyUser());
    }

    private void validateFormat(User userInfoToCreate) throws InvalidFormatException {
        userValidator.validateIdFormat(userInfoToCreate);
        userValidator.validateCreateAtFormat(userInfoToCreate);
        userValidator.validateUpdateAtFormat(userInfoToCreate);
        userValidator.validateNameFormat(userInfoToCreate);
        userValidator.validateEmailFormat(userInfoToCreate);
    }

    @Override
    public User deleteUserById(UUID key) {
        return Optional.ofNullable(data.remove(key))
                .orElse(User.createEmptyUser());
    }
}
