package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {

    UserStatus createUserStatus(UserStatus userStatusToCreate);

    UserStatus findUserStatusById(UUID key);

    List<UserStatus> findAllUserStatus();

    UserStatus updateUserStatus(UUID key, UserStatus userStatusToUpdate);

    UserStatus deleteUserStatusById(UUID key);
}
