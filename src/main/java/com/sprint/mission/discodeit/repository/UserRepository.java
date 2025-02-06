package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> save(User user);
    Optional<User> findByUserId(UUID userId);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void delete(UUID userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
