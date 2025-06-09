package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {

  @EntityGraph(attributePaths = {"profile"})
  void removeUserById(UUID id);

  @NonNull
  List<User> findAll();

  @EntityGraph(attributePaths = {"profile"})
  @NonNull
  Optional<User> findById(@NonNull UUID userId);

  @EntityGraph(attributePaths = {"profile"})
  List<User> findByIdIn(List<UUID> participantIds);

  @Query("select u from User u left join fetch u.profile")
  List<User> findUsers();

  @EntityGraph(attributePaths = {"profile"})
  User findUserByUsername(String username);

  @EntityGraph(attributePaths = {"profile"})
  User findUserByEmail(String email);

  boolean existsByUsername(String username);
}
