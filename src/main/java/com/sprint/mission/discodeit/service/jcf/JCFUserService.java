package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public void createUser(User user) {
        data.put(user.getId(), user);
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
    public void updateUser(UUID id, String userName) {
        User user = data.get(id);
        if (user != null) {
            user.update(userName);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }
}
