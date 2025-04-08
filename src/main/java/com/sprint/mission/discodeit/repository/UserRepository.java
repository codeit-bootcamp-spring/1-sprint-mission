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

  @EntityGraph(attributePaths = {"status", "profile"})
  void removeUserById(UUID id);

  @EntityGraph(attributePaths = {"status"})
  @NonNull
  List<User> findAll();

  @EntityGraph(attributePaths = {"status", "profile"})
  @NonNull
  Optional<User> findById(@NonNull UUID userId);

  @EntityGraph(attributePaths = {"status", "profile"})
  List<User> findByIdIn(List<UUID> participantIds);

  @Query("select u from User u left join fetch u.status left join fetch u.profile")
  List<User> findUsers();

  @EntityGraph(attributePaths = {"status", "profile"})
  User findUserByUsername(String username);

  @EntityGraph(attributePaths = {"status", "profile"})
  User findUserByEmail(String email);
}
