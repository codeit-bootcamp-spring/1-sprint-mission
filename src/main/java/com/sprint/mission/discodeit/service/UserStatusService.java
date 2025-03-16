package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UUID create(UUID userId);

    UserStatusDto find(UUID id);

    List<UserStatusDto> findAll();

    void update(UUID id);

    UserDto updateByUserId(UUID userId);

    void delete(UUID id);
}
