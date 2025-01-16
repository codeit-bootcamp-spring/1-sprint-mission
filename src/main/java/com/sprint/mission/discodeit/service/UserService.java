package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    public void creat(User user);

    public void delete(User user);

    public void update(User user, String name);

    public User write(String name);

    public List<User> allWrite();
}
