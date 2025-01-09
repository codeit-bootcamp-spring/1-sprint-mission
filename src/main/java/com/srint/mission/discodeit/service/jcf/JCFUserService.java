package com.srint.mission.discodeit.service.jcf;

import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public UUID create(User user) {
        data.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public User read(UUID id) {
        User user = data.get(id);
        return user;
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, User updatedUser) {
        if(data.containsKey(id)){
            updatedUser.setUpdatedAt(System.currentTimeMillis());
            data.put(id, updatedUser);
            return updatedUser;
        }
        throw new NoSuchElementException("not found");
    }

    @Override
    public UUID delete(UUID id) {
        User user = data.remove(id);
        return user.getId();
    }
}
