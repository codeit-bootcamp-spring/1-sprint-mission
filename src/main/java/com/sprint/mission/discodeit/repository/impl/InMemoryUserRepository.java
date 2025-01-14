package com.sprint.mission.discodeit.repository.impl;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {
    Map<UUID, User> users;
    public InMemoryUserRepository(){
        this.users = new HashMap<>();
    }
    public void saveUser(User user){
        users.put(user.getId(), user);
    }
    public User findById(UUID id){
        return users.get(id);
    }
    public Map<UUID, User> findAll() {
        return users;
    }
    public void deleteUser(UUID id){
        users.remove(id);
    }
}
