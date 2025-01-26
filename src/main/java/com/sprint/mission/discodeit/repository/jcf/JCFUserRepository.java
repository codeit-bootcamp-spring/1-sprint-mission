package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCFUserRepository implements UserRepository {
    private final Map<String, User> userData = new HashMap<>();

    @Override
    public User saveUser(User user) {
        userData.put(user.getEmail(), user);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        userData.remove(user.getEmail());
    }

    @Override
    public User findById(String email) {
        return userData.get(email);
    }

    @Override
    public List<User> printAllUser() {
        return new ArrayList<>(userData.values());
    }
}