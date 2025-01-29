package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String username, String email);
    User getUser(UUID id);
    List<User> getAllUsers();
    User updateUser(UUID id, String username, String email);
    void deleteUser(UUID id);
}
