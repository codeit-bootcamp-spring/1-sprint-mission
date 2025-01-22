package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    Map<UUID, User> users;
    public JCFUserRepository(){
        this.users = new HashMap<>();
    }
    @Override
    public void saveUser(User user){
        users.put(user.getId(), user);
    }
    @Override
    public Optional<User> findUserById(UUID id){
        return Optional.ofNullable(users.get(id));
    }
    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }
    @Override
    public void deleteAllUsers(){
        users.clear();
    }
    @Override
    public void deleteUserById(UUID id){
        users.remove(id);
    }
}
