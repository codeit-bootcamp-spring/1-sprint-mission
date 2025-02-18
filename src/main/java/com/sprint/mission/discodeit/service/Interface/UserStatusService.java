package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    void create(UserStatusRequest request);
    UserStatus find(UUID id);
    List<UserStatus> findAll();
    void update(UserStatusUpdateRequest request);
    void updateByUserId(UUID userId, String  status);
    void delete(UUID id);
    void deleteByUserId(UUID userId);
}
