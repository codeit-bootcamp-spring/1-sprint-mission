package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    List<User> findAllByIdIn(List<UUID> ids);
    Optional<User> findByUsername(String username);
    List<User> readAllContents();
    boolean existsById(UUID id);
    public boolean existsByEmail(String email);
    public boolean existsByUsername(String username);
    void deleteById(UUID id);
}
