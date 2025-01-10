package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class JCFUserService implements UserService {
    private final Map<String, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User create(User user) {
        data.put(user.getId().toString(), user);
        return user;
    }

    @Override
    public User findById(String id) {
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(String id, User user) {
        if (data.containsKey(id)) {
            data.put(id, user);
            return user;
        }
        return null;
    }

    @Override
    public void delete(String id) {
        data.remove(id);
    }
}
