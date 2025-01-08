package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void createUser(String username);
    UUID getUser(UUID id);
    List<User> getAllUsers();
    void updateUser(UUID id, String username);
    void deleteUser(UUID id);
}
