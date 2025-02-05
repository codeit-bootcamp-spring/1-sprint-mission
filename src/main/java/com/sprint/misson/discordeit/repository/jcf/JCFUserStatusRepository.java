package com.sprint.misson.discordeit.repository.jcf;

import com.sprint.misson.discordeit.entity.status.UserStatus;
import com.sprint.misson.discordeit.repository.UserStatusRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCFUserStatusRepository implements UserStatusRepository {

    private final Map<String, UserStatus> data;

    public JCFUserStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        return data.put(userStatus.getId(), userStatus);
    }

    @Override
    public UserStatus findById(String id) {
        return data.get(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public boolean delete(String id) {
        return data.remove(id) != null;
    }
}
