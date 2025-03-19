package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  @EntityGraph(attributePaths = {"userStatus", "profile"})
  List<User> findAll();

  @Query("SELECT u FROM User u "
      + "LEFT JOIN FETCH u.userStatus us "
      + "LEFT JOIN FETCH u.profile p")
  List<User> findAllWithFetchJoin();
}
