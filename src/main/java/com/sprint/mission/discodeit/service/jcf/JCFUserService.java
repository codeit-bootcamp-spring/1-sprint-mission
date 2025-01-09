package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;
    public JCFUserService(){
        this.data = new HashMap<>();
    }

    @Override
    public User save(User user){
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> readAll(){
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, User user){
        if (!data.containsKey(id)) {
            throw new NoSuchElementException("User not found");
        }
        data.put(id, user);
        return user;
    }

    @Override
    public boolean delete(UUID id){
        if (!data.containsKey(id)) {
            return false;
        }
        data.remove(id);
        return true;
    }
}
