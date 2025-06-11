package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  @Override
  @EntityGraph(attributePaths = {"profile"})
  Optional<User> findById(UUID uuid);

  @EntityGraph(attributePaths = {"profile"})
  List<User> findAll();

  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.profile WHERE u.username = :username")
  Optional<User> findByUsernameWithProfile(String username);
}
