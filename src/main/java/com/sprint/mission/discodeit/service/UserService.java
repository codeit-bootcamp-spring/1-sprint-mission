package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    Optional<User> readUser(User user);
    List<User> readAllUsers();
    User updateUser(User existUser, User updateUser);
    boolean deleteUser(User user);
}
