package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void create(User user);
    void update(UUID id, User user);
    void delete(UUID id);
    List<User> readAll();
}
