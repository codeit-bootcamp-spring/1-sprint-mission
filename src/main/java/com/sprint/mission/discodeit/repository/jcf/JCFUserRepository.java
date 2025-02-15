package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> users;

    public JCFUserRepository() {
        this.users = new HashMap<>();
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public boolean existsById(UUID userId) {
        return users.containsKey(userId);
    }
}
