package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    List<User> findAll();
    boolean existsById(UUID id);
    boolean existsByUsername(String name);
    boolean existsByEmail(String email);
    void deleteById(UUID id);
    void delete(User user);
    Optional<User> findByUsername(String username);
    List<User> findAllById(List<UUID> ids);
}
