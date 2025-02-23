package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.FindUserResponseDto;
import com.sprint.mission.discodeit.dto.userStatusDto.FindUserStatusResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UUID create(UUID userId);

    FindUserStatusResponseDto find(UUID id);

    List<FindUserStatusResponseDto> findAll();

    void update(UUID id);

    FindUserResponseDto updateByUserId(UUID userId);

    void delete(UUID id);
}
