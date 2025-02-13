package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusDto create(UserStatusCreateRequest request);
    UserStatusDto findById(UUID UserStatusId);
    List<UserStatusDto> findAll();
    UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request);
    UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request);
    void delete(UUID userStatusId);
}
