package com.sprint.mission.discodeit.repository.jpa.userStatus;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusJpaRepository extends JpaRepository<UserStatus, UUID> {

  Optional<UserStatus> findUserStatusByUser(User user);
}
