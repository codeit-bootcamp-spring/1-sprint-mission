package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

  //TODO: n+1 문제 해결
  UserStatus findByUserId(UUID userId);

  Boolean existsByUserId(UUID userId);

}
