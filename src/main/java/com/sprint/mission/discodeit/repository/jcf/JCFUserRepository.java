package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID,User> data;

    public JCFUserRepository() {
        this.data = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public void createUser(User user) {
        this.data.put(user.getId(), user);
    }


    @Override
    public Optional<User> getUserById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, User updateUser) {
        User existingUser = data.get(id);
        if (existingUser != null) {
            existingUser.update(updateUser.getName(), updateUser.getEmail(), updateUser.getPassword());
        }
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }
}
