package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    Optional<User> readUser(UUID existUserId);
    List<User> readAllUsers();
    User updateUser(UUID existUerId, User updateUser);
    boolean deleteUser(User user);
}
