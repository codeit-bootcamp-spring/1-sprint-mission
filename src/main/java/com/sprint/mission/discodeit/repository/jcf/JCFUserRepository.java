package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Profile("jcf")
@Repository
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> userMap;

    public JCFUserRepository() {
        this.userMap = new HashMap<>();
    }

    @Override
    public User save(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean userExistById(UUID userID) {
        return userMap.containsKey(userID);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userMap.values().stream()
                .filter(user -> user.getPhone().equals(phone))
                .findFirst();
    }

    @Override
    public Optional<User> findByName(String name) {
        return userMap.values().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public List<User> findAll() {
        return userMap.values().stream().toList();
    }

    @Override
    public List<User> findAllById(List<UUID> userIds) {
        return userIds.stream()
                .map(userMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        userMap.remove(id);
    }
}
