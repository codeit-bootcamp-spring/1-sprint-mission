package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void addUser(User user);

    User getUser(UUID id);

    List<User> getAllUsers();

    void updateUser(UUID id, String newUsername, String newPassword);

    void deleteUser(UUID id);
}
