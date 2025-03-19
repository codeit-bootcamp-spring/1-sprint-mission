package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.UserStatus;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

  UserStatus save(UserStatus userStatus);

  void deleteById(UUID id);

  Optional<UserStatus> findById(UUID id);

  Optional<UserStatus> findByUserId(UUID userId);

  List<UserStatus> findAll();

  // 존재 검증
  boolean existsById(UUID id);

  boolean existsByUserId(UUID userId);

  @Transactional
  @Modifying
  @Query("UPDATE UserStatus us SET us.lastActiveAt = :newLastActiveAt WHERE us.id =:id")
  int updateById(@Param("id") UUID id, @Param("newLastActiveAt") Instant lastActiveAt);
}
