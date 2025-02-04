package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data = new HashMap<>();

    // 사용자 저장
    @Override
    public void save(User user) {
        data.put(user.getId(), user);
    }

    // 특정 사용자 조회
    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    // 모든 사용자 조회
    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    // 사용자 수정
    @Override
    public void update(UUID id, User user) {
        if (data.containsKey(id)) {
            data.put(id, user);
        } else {
            throw new IllegalArgumentException("User not found: " + id);
        }
    }

    // 사용자 삭제
    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
