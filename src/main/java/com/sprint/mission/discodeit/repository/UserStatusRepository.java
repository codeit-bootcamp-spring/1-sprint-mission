package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.HashMap;
import java.util.UUID;

public interface UserStatusRepository {
    public HashMap<UUID, UserStatus> getUserStatusMap();
    public UserStatus getUserStatusById(UUID UserId);
    public boolean addUserStatus(UserStatus UserStatus);
    public boolean deleteUserStatusById(UUID UserId);
    public boolean isUserStatus(UUID UserId);
    public boolean updateUserStatus(UUID UserStatus);
}
