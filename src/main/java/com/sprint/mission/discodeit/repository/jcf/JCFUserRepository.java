package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class JCFUserRepository implements UserRepository {
    private final Map<Long, User> data;
    private final AtomicLong idGenerator = new AtomicLong(1);   // ID 초기값 1

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Long saveUser(User user) {
        Long id = idGenerator.getAndIncrement();
        data.put(id, user);
        return id;
    }

    public void saveUser(Long id, User user) {
        data.put(id, user);
    }

    @Override
    public User loadUser(Long id) {
        return data.get(id);
    }

    @Override
    public Map<Long, User> loadAllUsers() {
        return data;
    }

    @Override
    public void deleteUser(Long id) {
        if (data.remove(id) == null) {
            throw new IllegalArgumentException("삭제할 사용자가 존재하지 않습니다.");
        }
    }

    @Override
    public void updateUser(Long id, User user) {
        data.put(id, user);
    }
}
