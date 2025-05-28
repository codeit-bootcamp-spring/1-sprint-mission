package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

  @Query("SELECT u FROM User u "
      + "left JOIN FETCH u.status "
      + "LEFT JOIN FETCH u.profile "
      + "WHERE u.id = :id")
  Optional<User> findByIdWithProfileAndStatus(@Param("id") UUID id);

  @Query("SELECT u FROM User u "
      + "JOIN FETCH u.status "
      + "LEFT JOIN FETCH u.profile ")
  List<User> findAllWithProfileAndStatus();

  @Query("SELECT u FROM User u "
      + "LEFT JOIN FETCH u.status "
      + "LEFT JOIN FETCH u.profile "
      + "WHERE u.username = :username")
  Optional<User> findByUsername(@Param("username") String username);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);
}
