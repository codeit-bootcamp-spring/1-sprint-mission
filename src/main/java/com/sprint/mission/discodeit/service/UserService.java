package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void createUser(String password, String name);

    Optional<User> findUserById(UUID id);

    Optional<User> findUserByName(String name);

    List<User> findAllUsers();

    void updateUserName(UUID id, String newName);

    void updateUserPassword(UUID id, String newPassword);

    void removeUser(UUID id);
}
