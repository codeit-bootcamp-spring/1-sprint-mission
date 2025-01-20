package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class JCFUserService implements UserService {
    private final List<User> data;

    public JCFUserService() {
        this.data = new ArrayList<>();
    }

    @Override
    public void createUser(User user) {
        data.add(user);
    }

    @Override
    public Optional<User> readUser(UUID id) {
        return data.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> readAllUsers() {
        return new ArrayList<>(data);
    }

    @Override
    public void updateUser(UUID id, String name, int age, char gender) {
        readUser(id).ifPresent(user -> user.update(name, age, gender));
    }

    @Override
    public void deleteUser(UUID id) {
        data.removeIf(user -> user.getId().equals(id));
    }
}