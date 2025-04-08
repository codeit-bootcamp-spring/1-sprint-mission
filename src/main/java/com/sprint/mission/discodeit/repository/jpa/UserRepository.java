package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  @Override
  @EntityGraph(attributePaths = {"status", "profile"})
  Optional<User> findById(UUID uuid);

  @EntityGraph(attributePaths = {"status", "profile"})
  List<User> findAll();

}
