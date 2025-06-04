package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

//    @EntityGraph(attributePaths = "profile")
//    List<User> findAll();

    @Query("SELECT u FROM User u " + "LEFT JOIN FETCH u.profile")
    List<User> findAllWithProfile();

    boolean existsById(UUID id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String name);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("select u from  User u left join fetch u.profile where u.id= :id")
    Optional<User> findByIdWithProfile(@Param("id") UUID id);
}
