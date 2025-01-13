package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(User user);
    User read(UUID id);
    List<User> readAll();
    User update(UUID id, String username, String email);
    boolean delete(UUID id);
}
