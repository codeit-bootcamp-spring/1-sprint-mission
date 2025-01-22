package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public void creat(User user);

    public void delete(UUID userId);

    public void update(UUID userId, String name);

    public UUID write(String name);

    public List<User> allWrite();

    public String getName(UUID userId);
}
