package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> userData = new HashMap<>();

    @Override
    public User save(User user) {
        userData.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userData.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userData.values());
    }

    @Override
    public void delete(UUID id) {
        userData.remove(id);
    }
}
