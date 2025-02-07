package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> userList;


    public JCFUserRepository() {
        this.userList = new HashMap<>();
    }

    @Override
    public User save(User user) {
        userList.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User findbyId(UUID id) {
        return userList.get(id);
    }

    @Override
    public Map<UUID, User> load() {
        return userList;
    }

    @Override
    public void delete(UUID id) {
        userList.remove(id);
    }
}
