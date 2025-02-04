package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.IllegalUserException;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.validate.ServiceValidator;
import com.sprint.mission.discodeit.service.validate.UserServiceValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BasicUserService implements UserService {
    private static final EntityFactory entityFactory = BaseEntityFactory.getInstance();
    private final ServiceValidator<User> validator = new UserServiceValidator();
    private final UserRepository userRepository;


    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String userName, String userEmail, String userPassword) {
        if(validator.isNullParam(userName, userPassword)) {
            throw new IllegalUserException();
        }

        User user = validator.entityValidate(entityFactory.createUser(userName, userEmail, userPassword));
        userRepository.userSave(user);

        return user;
    }

    @Override
    public User getUserById(UUID userId) {
        return validator.entityValidate(userRepository.findUserById(userId));
    }

    /**
     * @return Map<UUID, User>
     * @Description: UserList를 찾지 못하면 비어있는 hashMap 반환
     */
    @Override
    public Map<UUID, User> getAllUsers() {
        return Optional.ofNullable(userRepository.findAllUser())
                .orElse(new HashMap<>());
    }

    @Override
    public User updateUser(UUID userId, String newName, String newEmail, String newPassword) {
        User findUser = getUserById(userId);

        findUser.updateName(newName);
        findUser.updateEmail(newEmail);
        findUser.updatePassword(newPassword);

        return userRepository.userSave(findUser);
    }

    /**
     * @param userId
     * @Description: 찾는 유저가 없으면 예외, 있다면 삭제
     */
    @Override
    public void deleteUser(UUID userId) {
        User findUser = getUserById(userId);

        userRepository.removeUserById(findUser.getUserId());
    }
}
