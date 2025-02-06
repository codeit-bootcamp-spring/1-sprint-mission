package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> userData;

    public JCFUserRepository(Map<UUID, User> userData) {
        this.userData = userData;
    }

    @Override
    public User save(User user) {
        userData.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userData.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userData.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found User")));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userData.values());
    }


    @Override
    public void deleteById(UUID id) {
        userData.remove(id);

    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userData.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not User Found")));
    }

    @Override
    public boolean existsByPassword(String password) {
        return userData.values().stream().anyMatch(user -> user.getPassword().equals(password));
    }

    @Override
    public boolean existsById(UUID id) {
        return userData.containsKey(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userData.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

}
