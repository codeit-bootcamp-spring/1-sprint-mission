package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.UUID;
import java.util.HashMap;

public class JCFUserRepository implements UserRepository {
    private final HashMap<UUID, User> data = new HashMap<>();

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public User findById(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public HashMap<UUID, User> findAll() {
        return new HashMap<>(data);
    }

    @Override
    public void delete(UUID uuid) {
        data.remove(uuid);
    }
}
