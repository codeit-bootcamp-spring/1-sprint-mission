package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

  boolean existsByUsername(String username);

  boolean existsUserByUsername(String username);

  boolean existsUserByEmail(String email);

  Optional<User> findByEmail(String email);

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.userStatus LEFT JOIN FETCH u.profileImage")
  List<User> findWithStatusAndProfile();

  List<User> findAllByRole(Role role);
}
