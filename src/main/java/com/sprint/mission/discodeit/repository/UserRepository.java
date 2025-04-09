
package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  User save(User user);

  @EntityGraph(attributePaths = {"profile", "userStatus"})
  Optional<User> findById(UUID userId);

  @EntityGraph(attributePaths = {"profile", "userStatus"})
  Optional<User> findByUsername(String userName);

  boolean existsById(UUID userId);

  @EntityGraph(attributePaths = {"profile", "userStatus"})
  List<User> findAll();

  void deleteById(UUID userId);

  boolean existsByEmail(String email);

  boolean existsByUsername(String userName);
}

