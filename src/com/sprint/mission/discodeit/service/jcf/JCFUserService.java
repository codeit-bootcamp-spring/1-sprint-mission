package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;

import java.util.UUID;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;
    private final UserValidator  userValidator;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        userValidator       = UserValidator.getInstance();
    }

    /**
     * Create the User while ignoring the {@code createAt} and {@code updateAt} fields from {@code userInfoToCreate}
     */
    @Override
    public User createUser(User userInfoToCreate) throws InvalidFormatException {
        validateFormat(userInfoToCreate);

        return userRepository.createUser(userInfoToCreate);
    }

    @Override
    public User findUserById(UUID key) {
        return userRepository.findUserById(key);
    }

    /**
     * Update the User while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields from {@code userInfoToUpdate}
     */
    @Override
    public User updateUserById(UUID key, User userInfoToUpdate) throws InvalidFormatException {
        validateFormat(userInfoToUpdate);

        return userRepository.updateUserById(key, userInfoToUpdate);
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
        return userRepository.deleteUserById(key);
    }
}
