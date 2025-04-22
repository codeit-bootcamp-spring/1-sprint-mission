package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusResponseDto create(UserStatus userStatus);

    List<UserStatusResponseDto> findAll();

    UserStatusResponseDto update(UUID userId, UserStatusRequestDto userStatusRequestDto);

    UserStatusResponseDto delete(UUID uuid);
}
