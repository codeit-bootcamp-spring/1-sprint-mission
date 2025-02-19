package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jsf")
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
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(this.data.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public boolean existsByUsername(String name) {
        return this.data.values().stream().anyMatch(user -> user.getUsername().equals(name));
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.data.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public void deleteById(UUID id) {
        this.data.remove(id);
    }

    @Override
    public void delete(User user) {
        this.data.remove(user.getId());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.data.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAllById(List<UUID> ids) {
        return ids.stream()
                .map(this.data::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
