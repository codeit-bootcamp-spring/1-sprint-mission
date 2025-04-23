package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

  // 유저 id로 접속 상태 검색
  Optional<UserStatus> findByUserId(UUID userId);
}
