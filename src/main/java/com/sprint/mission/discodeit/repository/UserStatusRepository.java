package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {
}
