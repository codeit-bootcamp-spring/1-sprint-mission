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
    public User create(String name, String email, String password) {
        if (userValidator.validateUser(name, email, password, findAll())){
            User user = new User(name, email,password);
            data.put(user.getId(), user);
            return user;
        }
        throw new CustomException(ExceptionText.USER_CREATION_FAILED);
    }


    @Override
    public User find(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID uuid, String email, String id, String password) {
        User user = find(uuid);
        if (user != null) {
            user.update(email, id, password);
            return user;
        }
        return null;
    }

    @Override
    public void delete(UUID uuid) {
        data.remove(uuid);
    }

}
