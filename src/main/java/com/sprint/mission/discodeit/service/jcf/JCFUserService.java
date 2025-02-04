package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;

import java.util.*;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data = new HashMap<>() ;

    private final UserValidator userValidator;

    public JCFUserService(UserValidator userValidator){

        this.userValidator = userValidator;
    }

    @Override
    public User CreateUser(String name, String email,String iD ,String password) {
        if (userValidator.validateUser(name, email, password, getAllUsers())){
            User user = new User(name, email, iD, password);
            data.put(user.getUuID(), user);
            return user;
        }
        throw new CustomException(ExceptionText.USER_CREATION_FAILED);
    }


    @Override
    public User getUser(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public HashMap<UUID, User> getAllUsers() {
        return new HashMap<>(data);
    }

    @Override
    public void updateUser(UUID uuid, String email, String id, String password) {
        User user = getUser(uuid);
        if (user != null) {
            user.update(email, id, password);
        }
    }

    @Override
    public void deleteUser(UUID uuid) {
        data.remove(uuid);
    }

}
