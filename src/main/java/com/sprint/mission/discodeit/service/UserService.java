package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(UUID id, Long createdAt, Long updatedAt, String name);
    User getUser(UUID id);
    List<User> getAllUsers();
    User updateUser(UUID id, String name, Long updatedAt);
    void deleteUser(UUID id);
}
