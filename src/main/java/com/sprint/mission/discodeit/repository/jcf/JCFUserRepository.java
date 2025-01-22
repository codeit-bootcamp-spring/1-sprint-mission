package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {
    Map<UUID, User> users;
    public JCFUserRepository(){
        this.users = new HashMap<>();
    }
    public void saveUser(User user){
        users.put(user.getId(), user);
    }
    public User findUserById(UUID id){
        return users.get(id);
    }
    public Map<UUID, User> getAllUsers() {
        return users;
    }
    public void deleteAllUsers(){
        users.clear();
    }
    public void deleteUserById(UUID id){
        users.remove(id);
    }
}
