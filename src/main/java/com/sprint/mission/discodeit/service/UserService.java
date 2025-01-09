package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(User user);
    Optional<User> read(User user);
    List<User> readAll();
    User update(User existUser, User updateUser);
    boolean delete(User user);
}
