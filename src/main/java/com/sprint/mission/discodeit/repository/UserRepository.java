package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  @Query("select m from User m join fetch m.userStatus join fetch m.profile")
  List<User> findAll();

  boolean existsById(UUID id);

  void deleteById(UUID id);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);
}
