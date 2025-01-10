package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<String, User> data = new HashMap<>();

    @Override
    public void create(User user) {
        data.put(user.getId().toString(), user);
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(String id, String newUsername) {
        User user = data.get(id);
        if (user != null) {
            user.updateUsername(newUsername);
        }
    }

    @Override
    public void delete(String id) {
        data.remove(id);
    }
}
