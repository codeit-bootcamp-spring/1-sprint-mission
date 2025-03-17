package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,UUID> {

  User save(User user);

  @Query("""
    SELECT new com.sprint.mission.discodeit.dto.data.UserDto(
        u.id, 
        u.createdAt, 
        u.updatedAt, 
        u.username, 
        u.email, 
        u.profileImage
    ) 
    FROM User u 
    WHERE u.id = :id
""")
  Optional<UserDto> findByIdQueary(@Param("username")UUID id);

  Optional<User> findById(UUID userId);

  Optional<User> findByUsername(String name);

  @Query("""
    SELECT new com.sprint.mission.discodeit.dto.data.UserDto(
        u.id, 
        u.createdAt, 
        u.updatedAt, 
        u.username, 
        u.email, 
        u.profileImage
    ) 
    FROM User u 
    WHERE u.username = :username
""")
  Optional<UserDto> findByUsernameQueary(@Param("username") String username);

  @Query("""
        SELECT new com.sprint.mission.discodeit.dto.data.UserDto(
            u.id, 
            u.createdAt, 
            u.updatedAt, 
            u.username, 
            u.email, 
            u.profileImage
        ) 
        FROM User u
    """)
  List<UserDto> findAllQueary();

  boolean existsById(UUID id);

  void deleteById(UUID id);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);
}
