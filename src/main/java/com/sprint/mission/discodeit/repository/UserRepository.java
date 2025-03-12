package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  @EntityGraph(attributePaths = {"status"})
  List<User> findAll();

  boolean existsById(UUID id);

  boolean existsByEmail(String email);

  boolean existsByUsername(String name);

  Optional<User> findByUsername(String username);
}
