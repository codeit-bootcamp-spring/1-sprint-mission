package com.sprint.mission.repository;

import com.sprint.mission.entity.main.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"status", "profile"})
    @Query("SELECT u FROM User u")
    List<User> findAllFetch();
}