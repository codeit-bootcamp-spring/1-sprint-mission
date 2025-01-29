package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User createUser(String username, String email) {
        User user = new User(username, email);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User updateUser(UUID id, String username, String email) {
        User user = data.get(id);
        if(user != null) {
            user.update(username, email);
            return user;
        }
        return null;
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }
}

