package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionCode;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;

import java.util.*;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data = new HashMap<>() ;

    @Override
    public User CreateUser(String name, String email,String iD ,String password) {
        if (!UserValidator.validateUser(name, email, password)) {
            throw new CustomException(ExceptionCode.USER_CREATION_FAILED);
        }
        User user = new User(name, email, iD, password);
        data.put(user.getuuID(), user);
        return user;
    }


    @Override
    public User getUser(UUID id) {
        return data.get(id);
    }

    @Override
    public HashMap<UUID, User> getAllUsers() {
        return new HashMap<>(data);
    }

    @Override
    public void updateUser(UUID uuId, String email, String id, String password) {
        User user = data.get(uuId);
        if (user != null) {
            user.update(email, id, password);
        }
    }

    @Override
    public void deleteUser(UUID useruuId) {data.remove(useruuId);
    }
}
