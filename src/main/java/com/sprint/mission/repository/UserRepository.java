package com.sprint.mission.repository;

import com.sprint.mission.entity.main.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"status", "profile"})
    @Query("SELECT u FROM User u")
    List<User> findAllWithRelations();


    @EntityGraph(attributePaths = {"status", "profile"})
    @NonNull
    Optional<User> findById(UUID id);

    @EntityGraph(attributePaths = {"status"})
    Optional<User> findWithStatusById(UUID id);

    @Query("SELECT u FROM User u JOIN FETCH u.readStatus rs JOIN FETCH rs.channel c WHERE u.id = :id")
    Optional<User> findWithStatusAndChannelById(UUID id);
}