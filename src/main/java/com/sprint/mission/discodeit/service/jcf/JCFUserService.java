package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;

import java.util.*;

public class JCFUserService implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public JCFUserService(UserRepository userRepository, UserValidator userValidator){
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    public User CreateUser(String name, String email,String iD ,String password) {
        if (userValidator.validateUser(name, email, password, userRepository.findAll())) {
            User user = new User(name, email, iD, password);
            userRepository.save(user);
            return user;
        }
        throw new CustomException(ExceptionText.USER_CREATION_FAILED);
    }


    @Override
    public User getUser(UUID uuid) {
        return userRepository.findById(uuid);
    }

    @Override
    public HashMap<UUID, User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID uuid, String email, String id, String password) {
        User user = userRepository.findById(uuid);
        if (user != null) {
            user.update(email, id, password);
        }
    }

    @Override
    public void deleteUser(UUID uuid) {
        userRepository.delete(uuid);
    }
}
