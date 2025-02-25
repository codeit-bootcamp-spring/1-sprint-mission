package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  User save(User user);

  Optional<User> getUserById(UUID id);

  List<User> getAllUsers();

  boolean existsById(UUID id);

  void deleteUser(UUID id);

  boolean existsByEmail(String email);

  boolean existsByName(String name);

  Optional<User> findByUsername(String username);
}
