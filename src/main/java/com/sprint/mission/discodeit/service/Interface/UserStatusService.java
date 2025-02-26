package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    void create(UserStatusCreateRequestDto request);
    UserStatus find(UUID id);
    List<UserStatus> findAll();
    void update(UUID userStatusId,UserStatusUpdateRequest request);
    void updateByUserId(UUID userId, UserStatusUpdateRequest request);
    void deleteByUserId(UUID userId);
}
