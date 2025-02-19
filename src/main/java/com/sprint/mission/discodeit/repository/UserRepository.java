package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(UUID id);
    Optional<User> findByName(String name);
    void deleteById(UUID id);
    boolean existsById(UUID id);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
