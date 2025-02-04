package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.validate.ServiceValidator;
import com.sprint.mission.discodeit.service.validate.UserServiceValidator;

import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    private static JCFUserService instance;
    private final UserRepository userRepository;
    private static EntityFactory entityFactory;

    private final ServiceValidator<User> validator = new UserServiceValidator();

    public JCFUserService(UserRepository userRepository) {
        JCFUserService.entityFactory = BaseEntityFactory.getInstance();
        this.userRepository = userRepository;
    }

    public JCFUserService() {
        entityFactory = BaseEntityFactory.getInstance();
        userRepository = new JCFUserRepository();
    }

    public JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }

        return instance;
    }

    @Override
    public User createUser(String userName, String userEmail, String userPassword) {
        if(validator.isNullParam(userName, userEmail, userPassword)) {
            throw new UserNotFoundException();
        }

        User user = validator.entityValidate(entityFactory.createUser(userName, userEmail, userPassword));

        userRepository.userSave(user);

        return user;
    }

    @Override
    public User getUserById(UUID userId) {
        return validator.entityValidate(userRepository.findUserById(userId));
    }

    @Override
    public Map<UUID, User> getAllUsers() {
        return validator.entityValidate(userRepository.findAllUser());
    }

    @Override
    public User updateUser(UUID userId, String newName, String newEmail, String newPassword) {
        validator.isNullParam(newName, newEmail, newPassword);

        User findUser = getUserById(userId);

        if (findUser == null) {
            throw new UserNotFoundException();
        } else {
            findUser.updateName(newName);
            findUser.updateEmail(newEmail);
            findUser.updatePassword(newPassword);
        }

        return findUser;
    }

    @Override
    public void deleteUser(UUID userId) {
        User findUser = validator.entityValidate(getUserById(userId));

        userRepository.removeUserById(findUser.getUserId());
    }
}