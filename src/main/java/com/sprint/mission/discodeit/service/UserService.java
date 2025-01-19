package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String username);

    User getUserById(UUID userId);
    List<User> getAllUsers();

    void updateUsername(UUID userId, String newUsername);

    void deleteUser(UUID userId);
}
