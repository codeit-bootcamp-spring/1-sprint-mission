package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsUserByEmail(String email);

    boolean existsUserByUsername(String username);

    User findByUsername(String username);

    boolean existsUserByUsernameAndPassword(String username, String password);

    //    @Query("select u from User u left join fetch u.userStatus left join fetch u.profile")
    @EntityGraph(attributePaths = {"userStatus", "profile"})
    @Query("select u from User u")
    List<User> findAllWithDetails();

    @EntityGraph(attributePaths = {"userStatus", "profile"})
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdWithDetails(@Param("id") UUID id);

    @Modifying
    @Query("delete from User u where u.createdAt > :createdAtAfter")
    void deleteUsersByCreatedAtAfter(@Param("createdAtAfter") Instant createdAtAfter);
}