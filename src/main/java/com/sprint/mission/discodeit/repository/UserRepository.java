package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);

    boolean userExistById(UUID userID);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    Optional<User> findById(UUID userId);

    List<User> findAll();

    List<User> findAllById(List<UUID> userIds);

    void delete(UUID id);
}
