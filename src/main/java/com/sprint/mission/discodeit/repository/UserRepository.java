//package com.sprint.mission.discodeit.repository;
//
//import com.sprint.mission.discodeit.entity.User;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//public interface UserRepository {
//
//  User save(User user);
//
//  Optional<User> findById(UUID id);
//
//  Optional<User> findByUsername(String username);
//
//  List<User> findAll();
//
//  boolean existsById(UUID id);
//
//  void deleteById(UUID id);
//
//  boolean existsByEmail(String email);
//
//  boolean existsByUsername(String username);
//}
package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  @Query("SELECT u FROM User u "
      + "LEFT JOIN FETCH u.profile "
      + "JOIN FETCH u.status")
  List<User> findAllWithProfileAndStatus();
}
