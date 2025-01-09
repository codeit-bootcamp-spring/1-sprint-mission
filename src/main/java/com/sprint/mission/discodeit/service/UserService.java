package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(User user);
    Optional<User> read(UUID id);
    List<User> readAll();
    User update(UUID id, User user);
    boolean delete(UUID id);
}
