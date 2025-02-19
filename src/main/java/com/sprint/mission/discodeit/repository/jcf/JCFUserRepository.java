package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository("jcfUserRepository")
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> userStorage = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        userStorage.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userStorage.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userStorage.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userStorage.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void deleteById(UUID id) { // ✅ 추가된 메서드
        userStorage.remove(id);
    }
}
