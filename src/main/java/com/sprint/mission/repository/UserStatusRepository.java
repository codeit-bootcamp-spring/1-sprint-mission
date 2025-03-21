package com.sprint.mission.repository;

import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {
    Optional<UserStatus> findByUser(User user);

    boolean existsByUser(User user);

    Optional<UserStatus> findByUser_Id(UUID userId);
}
//  UserStatus save(UserStatus userStatus);
//
//    Optional<UserStatus> findById(UUID id);
//
//    Optional<UserStatus> findByUserId(UUID userId);
//
//    List<UserStatus> findAll();
//
//    boolean existsById(UUID id);
//
//    void deleteById(UUID id);
//
//    void deleteByUserId(UUID userId);
