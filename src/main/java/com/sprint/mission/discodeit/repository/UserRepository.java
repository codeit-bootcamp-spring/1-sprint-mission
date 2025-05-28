package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

  @EntityGraph(attributePaths = {"profile"})
  Optional<User> findById(UUID id);

  @EntityGraph(attributePaths = {"profile"})
  List<User> findAll();

  @EntityGraph(attributePaths = {"profile"})
  List<User> findAllByIdIn(List<UUID> userIds);

  @Query("""
       SELECT u FROM User u
       LEFT JOIN FETCH u.profile
       WHERE u.username = :username
      """)
  Optional<User> findByUsernameWithProfileAndStatus(@Param("username") String username);

  Optional<User> findByUsername(String username);
}
