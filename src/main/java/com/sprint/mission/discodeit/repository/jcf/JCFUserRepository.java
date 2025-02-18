package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> userData = new HashMap<>();

    @Override
    public User saveUser(User user) {
        userData.put(user.getId(), user);
        return user;
    }

    @Override
    public User findUserById(UUID userId) {
        return userData.get(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(userData.values());
    }

    @Override
    public void removeUser(UUID userId) {
        userData.remove(userId);
    }

    @Override
    public boolean existsUser(UUID userId) {
        return userData.containsKey(userId);
    }

    @Override
    public Optional<User> findUserByName(String name) {
        return userData.values().stream().filter(user -> user.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userData.values().stream().filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findUserByNameAndPassword(String name, String password) {
        return userData.values().stream()
                .filter(user -> user.getName().equals(name) && user.getPassword().equals(password))
                .findFirst();
    }
}
