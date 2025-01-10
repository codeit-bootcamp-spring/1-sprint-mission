package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> userData = new HashMap<>();

    @Override
    public void create(User user) {
        userData.put(user.getId(),user);
    }

    @Override
    public User read(UUID id) {
        return userData.get(id);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(userData.values());
    }

    @Override
    public void update(UUID id, User user) {
        if(userData.containsKey(id)){
            user.setUpdatedAt(System.currentTimeMillis());
            userData.put(id,user);
        }
    }
    public void delete(UUID id){
        userData.remove(id);
    }
}
