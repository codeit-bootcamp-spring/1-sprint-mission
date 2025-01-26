package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JcfUserRepository implements UserRepository {

    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public void createUser(UUID id, User user) {
        data.put(user.getId(), user);
    }

    @Override
    public void updateUserName(UUID id, String name) {
        data.get(id).updateUserName(name);
    }

    @Override
    public void updateUserEmail(UUID id, String email) {
        data.get(id).updateUserEmail(email);
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }


}
