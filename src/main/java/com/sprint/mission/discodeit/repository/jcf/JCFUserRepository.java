package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Repository
@ConditionalOnProperty(name = "sprint-mission.repository.type", havingValue = "jcf")
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findById(UUID id) {
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return users.containsKey(id);
    }

//    @Override
//    public boolean existsByEmail(String email) {
//        for (User user : users.values()) {
//            if (user.getEmail().equals(email)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public boolean existsByUsername(String username) {
//        for (User user : users.values()) {
//            if (user.getUsername().equals(username)) {
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    public void deleteById(UUID id) {
        users.remove(id);
    }
}