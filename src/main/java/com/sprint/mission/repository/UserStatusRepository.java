package com.sprint.mission.repository;

import com.sprint.mission.entity.UserStatus;
import com.sprint.mission.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {
    Optional<UserStatus> findByUser(User user);
    boolean existsByUser(User user);
}

