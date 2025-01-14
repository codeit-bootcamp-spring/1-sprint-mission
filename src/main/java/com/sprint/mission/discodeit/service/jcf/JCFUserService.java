package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private static JCFUserService instance; // 싱글톤 인스턴스
    private final Map<UUID, User> data = new HashMap<>();

    private JCFUserService() {} // private 생성자

    public static JCFUserService getInstance() {
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
    public void create(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public Optional<User> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, User user) {
        if (data.containsKey(id)) {
            data.put(id, user);
        } else {
            throw new IllegalArgumentException("User not found for ID: " + id);
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
