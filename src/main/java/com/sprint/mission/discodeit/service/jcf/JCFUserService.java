package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public void createUser(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public User createUser(String userName, String email, String password) {
        UUID userId = UUID.randomUUID();
        long currentTime = System.currentTimeMillis();
        User user = new User(userId, System.currentTimeMillis(), System.currentTimeMillis(), userName, email, password); // User 객체 생성
        data.put(userId, user);
        return user;
    }

    @Override
    public User getUser(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, String userName) {
        User user = data.get(id);
        if (user != null) {
            user.update(userName);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }
}
