package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Primary
@Repository
public class JCFUserRepository implements UserRepository {
    private final Map<String, User> dataStore = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        dataStore.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(String id) {
        dataStore.remove(id);
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(dataStore.values());
    }
}