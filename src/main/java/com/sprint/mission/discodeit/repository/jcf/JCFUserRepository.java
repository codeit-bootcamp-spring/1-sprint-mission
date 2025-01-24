package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public void createUser(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public Optional<User> getUser(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, String userName) {
        User user = data.get(id);
        if (user == null) {
            throw new IllegalArgumentException("해당 iD의 유저를 찾을 수 없습니다: " + id);
        }
        user.update(userName);
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }
}
