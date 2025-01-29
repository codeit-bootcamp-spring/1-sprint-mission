package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;
    public JCFUserRepository(){
        this.data = new HashMap<>();
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public User findById(UUID userId) {
        User user = this.data.get(userId);
        return Optional.ofNullable(user).orElseThrow(() -> new NoSuchElementException(userId + "를 찾을 수 없습니다."));
    }

    @Override
    public void delete(UUID userId) {
        data.remove(userId);
    }

    @Override
    public Map<UUID, User> findAll() {
        return new HashMap<>(data);
    }
}
