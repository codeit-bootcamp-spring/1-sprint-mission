package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User newUser);

    List<User> getAllUsers();

    User getUserById(UUID id);

    void delete(UUID id);

    void save();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> getUserByEmail(String userEmail1);
}
