package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFUserService implements UserService {
    private static volatile JCFUserService instance;
    private final Map<UUID, User> data;

    private JCFUserService() {
        this.data = new ConcurrentHashMap<>();
    }

    protected static JCFUserService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFUserService();
                }
            }
        }
        return instance;
    }

    @Override
    public User register(User user) {
        data.put(user.getId(), user); //저장
        return user;
    }

    @Override
    public Optional<User> getUserDetails(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean updateUserProfile(UUID id, String name, String email, String password) {
        return data.computeIfPresent(id, (key, user) -> {
            user.update(name, email, password);
            return user;
        }) != null;
    }

    @Override
    public boolean deleteUser(UUID id) {
        return data.remove(id) != null;
    }
}
