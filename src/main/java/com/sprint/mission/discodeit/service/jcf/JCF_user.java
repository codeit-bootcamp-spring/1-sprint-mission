package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCF_user implements UserService {

    private final List<User> userSet;

    public JCF_user() {
        userSet = new ArrayList<>();
    }
    @Override
    public void Creat(User user) {
        userSet.add(user);

    }

    @Override
    public void Delete(User user) {
        userSet.remove(user);
    }

    @Override
    public void Update(User user, User updateUser) {
        userSet.replaceAll(users -> users.equals(user) ? updateUser : users);
    }

    @Override
    public List<User> Write(UUID id) {
        return userSet.stream().filter(user_id -> user_id.GetId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public List<User> AllWrite() {
        return userSet;
    }
}
