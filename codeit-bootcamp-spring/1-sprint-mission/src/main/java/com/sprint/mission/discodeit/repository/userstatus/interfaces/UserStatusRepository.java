package com.sprint.mission.discodeit.repository.userstatus.interfaces;

import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import java.util.Optional;

public interface UserStatusRepository {

    UserStatus save(UserStatus userStatus);

    Optional<UserStatus> findByUser(User user);
}
