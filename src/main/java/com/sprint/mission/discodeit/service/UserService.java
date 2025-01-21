package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.UUID;

public interface UserService {
    User createUser(String userName, String userEmail, String userPassword); // 저장

    User getUserById(UUID userId); // 저장

    Map<UUID, User> getAllUsers(); // 저장

    User updateUser(UUID userId, String newName, String newEmail, String newPassword); // 비즈니스

    void deleteUser(UUID userId); // 저장

}
