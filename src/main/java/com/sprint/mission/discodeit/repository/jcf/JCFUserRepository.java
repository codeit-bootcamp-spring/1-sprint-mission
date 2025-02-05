package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.List;

public class JCFUserRepository implements UserRepository {

    private final HashMap<String, User> data;

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean delete(User user) {
        return data.remove(user.getId()) != null;
    }

    @Override
    public User findById(String id) {
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }
}
