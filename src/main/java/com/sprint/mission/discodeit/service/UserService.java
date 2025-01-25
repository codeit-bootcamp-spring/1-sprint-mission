package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(String name, String email, String password);

    List<User> getAllUserList();

    User searchById(UUID userId);

    void updateUserName(UUID userId, String newName);

    void updateUserEmail(UUID userId, String newEmail);

    void deleteUser(UUID userId);
}
