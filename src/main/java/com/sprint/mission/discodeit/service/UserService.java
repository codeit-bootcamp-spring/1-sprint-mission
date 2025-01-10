package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String userName, String userEmail, String userPassword);

    User getUserById(UUID userId);

    List<User> getAllUsers();

    User updateUser(UUID userId, String newName, String newEmail, String newPassword);

    void deleteUser(UUID userId);
}
