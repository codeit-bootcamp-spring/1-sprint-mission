package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository("jcfUserRepository")
public class JCFUserRepository {
    private final Map<UUID, User> userStorage = new ConcurrentHashMap<>();

    public void save(User user) {
        userStorage.put(user.getId(), user);
    }

    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userStorage.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(userStorage.values());
    }

    public Optional<User> findByUsername(String username) {
        return userStorage.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return userStorage.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public void deleteById(UUID id) {
        userStorage.remove(id);
    }
}
