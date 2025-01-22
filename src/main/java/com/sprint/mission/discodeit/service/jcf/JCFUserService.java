package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public void createUser(User user) {
        data.put(user.getId(), user);
        System.out.println("[User Created]: " + user);
    }

    @Override
    public Optional<User> getUser(UUID id) {
        Optional<User> user = Optional.ofNullable(data.get(id));
        System.out.println("[User Retrieved]: " + user.orElse(null));
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>(data.values());
        System.out.println("[All Users Retrieved]: " + users);
        return users;
    }

    @Override
    public void updateUser(User user) {
        if (data.containsKey(user.getId())) {
            data.put(user.getId(), user);
            System.out.println("[User Updated]: " + user);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        User removedUser = data.remove(id);
        System.out.println("[User Deleted]: " + removedUser);
    }
}