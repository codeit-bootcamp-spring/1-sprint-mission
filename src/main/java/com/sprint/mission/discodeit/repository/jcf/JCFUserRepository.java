package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> users;

    public JCFUserRepository(){
        this.users = new HashMap<>();
    }

    @Override
    public User save(User user){
        this.users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(UUID id){
        this.users.remove(id);
    }

    @Override
    public List<User> findAll(){
        return this.users.values().stream()
                .toList();
    }

    @Override
    public boolean existsId(UUID id) {
        return this.users.containsKey(id);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(this.users.get(id));
    }
}
