package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  User save(User user);

  Optional<User> findById(UUID userId);

  Optional<User> findByUsername(String username);

  List<User> findAll();

  void deleteById(UUID userId);

  boolean existsById(UUID userId);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);
}
