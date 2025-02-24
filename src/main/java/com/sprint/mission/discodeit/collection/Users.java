package com.sprint.mission.discodeit.collection;

import com.sprint.mission.discodeit.entity.User;
import java.util.*;

public class Users {
    private final Map<UUID, User> users = new HashMap<>();

    // 사용자 추가
    public Optional<User> add(UUID id, User user) {
        users.put(id, user);
        return Optional.of(user);
    }

    // 사용자 삭제
    public Optional<User> remove(UUID id) {
        return Optional.ofNullable(users.remove(id));
    }

    // 사용자 수정
    public Optional<User> update(UUID id, String newUsername) {
        User user = users.get(id);
        if (user != null) {
            user.updateUsername(newUsername);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    // 사용자 단건 조회
    public Optional<User> get(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    // 사용자 개수 조회
    public int size() {
        return users.size();
    }

    // 읽기 전용 맵 반환
    public Map<UUID, User> asReadOnly() {
        return Collections.unmodifiableMap(users);
    }
}
