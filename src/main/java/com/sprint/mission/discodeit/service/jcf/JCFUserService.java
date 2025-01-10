package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data = new HashMap<>();

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

    // 추가: 데이터가 없을 때 예외를 발생시키는 메서드
    public User findByIdOrThrow(UUID id) {
        return data.getOrDefault(id, null);
    }
}
