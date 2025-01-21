package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<String, User> data = new HashMap<>();

    @Override
    public void saveAll(List<User> users) {
        users.forEach(user -> data.put(user.getId().toString(), user));
    }

    @Override
    public List<User> loadAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void reset() {
        data.clear(); // 메모리 데이터를 초기화
        System.out.println("메모리 데이터가 초기화되었습니다.");
    }
}
