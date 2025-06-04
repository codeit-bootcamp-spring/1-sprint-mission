package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  boolean existsUserByEmail(String email);

  boolean existsUserByUsername(String username);

  User findByUsername(String username);

  Optional<User> findByEmail(String email);

  boolean existsUserByUsernameAndPassword(String username, String password);

  //    @Query("select u from User u left join fetch u.userStatus left join fetch u.profile")
  @EntityGraph(attributePaths = "profile")
  @Query("select u from User u")
  List<User> findAllWithDetails();

  @EntityGraph(attributePaths = "profile")
  @Query("select u from User u where u.id = :id")
  Optional<User> findByIdWithDetails(@Param("id") UUID id);

  @Modifying
  @Query("delete from User u where u.createdAt > :createdAtAfter")
  void deleteUsersByCreatedAtAfter(@Param("createdAtAfter") Instant createdAtAfter);
}
