package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdate;
import com.spirnt.mission.discodeit.enity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UUID create(UserStatusCreate userStatusCreate);

    UserStatus find(UUID userStatusId);

    List<UserStatus> findAll();

    void update(UUID userStatusId, UserStatusUpdate userStatusUpdate);

    void updateByUserId(UUID userId, UserStatusUpdate userStatusUpdate);
    void delete(UUID userStatusId);
}
