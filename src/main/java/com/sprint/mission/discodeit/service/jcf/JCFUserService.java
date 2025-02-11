package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.dto.user.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.CreatedUserDataDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
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
    public User createUser(CreatedUserDataDTO userData, BinaryContentDTO binaryCData) {
        try{
            userValidator.validateUser(userData.name(), userData.email(), userData.password());
        }catch (CustomException e){
            System.out.println("유저생성 실패 ->" + e.getMessage());
        }
        User user = new User(userData.name(), userData.email(), userData.id(), userData.password());
        data.put(user.getId(), user);
        return user;


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
        }
        return null;
    }

    @Override
    public void delete(UUID uuid) {
        data.remove(uuid);
    }

}
