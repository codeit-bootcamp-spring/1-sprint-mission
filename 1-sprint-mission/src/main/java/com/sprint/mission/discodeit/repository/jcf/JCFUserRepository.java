package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public User save(User user) {
        this.data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        boolean exists = this.existsById(id);
        if (!exists) {
            throw new IllegalArgumentException("User not found");
        }
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public List<User> findAll() {
        List<User> users = this.data.values().stream().toList();
        if(users.isEmpty()) {
            System.out.println("No users found");
            return Collections.emptyList();
        }
        return users;
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        boolean exists = this.existsById(id);
        if(!exists) {
            throw new IllegalArgumentException("User not found");
        }
        System.out.println("User deleted: " + id);
        this.data.remove(id);
    }
}
