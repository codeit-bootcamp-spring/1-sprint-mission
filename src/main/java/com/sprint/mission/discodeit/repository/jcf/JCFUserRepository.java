package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.collection.Users;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFUserRepository implements UserRepository {
    private final Users users = new Users();

    @Override
    public User save(User newUser) {
        users.add(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public List<User> getAllUsers() {
        return users.getUsersList();
    }

    @Override
    public User getUserById(UUID id) {
        return users.get(id).orElse(null);
    }

    @Override
    public void deleteById(UUID id) {
        users.remove(id);
    }

    @Override
    public void save() {
    }

    @Override
    public boolean existsByUsername(String username) {
        return users.getUsersList().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.getUsersList().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return users.getUsersList().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public boolean existsById(UUID id) {
        return users.get(id).isPresent();
    }
}
