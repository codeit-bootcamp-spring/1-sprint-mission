package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<User> findByName(String name) {
        return data.values().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}
