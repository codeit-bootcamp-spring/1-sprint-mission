package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String name, String email, String password);
    List<User> getAllUserList();
    User searchById(UUID id);
    void updateUser(UUID id, String newName, String newEmail, String newPassword);
    void deleteUser(UUID id);
}
