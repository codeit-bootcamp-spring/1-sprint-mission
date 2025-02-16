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
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    void deleteById(UUID id);
}
