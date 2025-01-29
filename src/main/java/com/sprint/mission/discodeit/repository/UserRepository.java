package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void createUser(User user);
    Optional<User> getUser(UUID id);
    List<User> getAllUsers();
    void updateUser(UUID id, String userName);
    void deleteUser(UUID id);
}
