package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void createUser(UUID id, User user);
    void updateUserName(UUID id, String name);
    void updateUserEmail(UUID id, String email);
    void deleteUser(UUID id);
    Optional<User> findById(UUID id);
    List<User> findAll();
}
