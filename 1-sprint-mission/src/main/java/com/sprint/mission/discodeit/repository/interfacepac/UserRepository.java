package com.sprint.mission.discodeit.repository.interfacepac;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    boolean existsById(UUID id);
    boolean existsByEmail(String email);
    boolean existsByPassword(String password);
    boolean existsByUsername(String username);
    void deleteById(UUID id);
}
