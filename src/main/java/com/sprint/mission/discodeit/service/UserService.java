package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User register(User user);

    Optional<User> getUserDetails(UUID id);

    List<User> getAllUsers();

    boolean updateUserProfile(UUID id, String name, String email, String password);

    boolean deleteUser(UUID id);
}
