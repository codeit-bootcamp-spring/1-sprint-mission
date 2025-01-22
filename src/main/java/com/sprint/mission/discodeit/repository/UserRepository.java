package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findUserById(UUID id);

    Optional<User> findUserByName(String name);

    List<User> findAllUsers();

    void save(User user);

    void remove(UUID id);
}
