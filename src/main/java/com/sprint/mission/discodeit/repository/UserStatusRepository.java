package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus saveUserStatus(UserStatus userStatus);
    UserStatus findUserStatusById(UUID userStatusId);
    Optional<UserStatus> findUserStatusByUser(User user);
    List<UserStatus> findAllUserStatuses();
    void removeUserStatus(UUID userStatusId);
}
