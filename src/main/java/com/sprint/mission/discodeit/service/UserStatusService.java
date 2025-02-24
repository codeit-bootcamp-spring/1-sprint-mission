package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusDTO;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    Long create(UserStatusDTO.request userStatusDTO);

    UserStatusDTO.response find(Long id);

    UserStatusDTO.response find(UUID uuid);

    UserStatusDTO.response findByUserId(UUID userid);

    List<UserStatusDTO.response> findAll();

    UserStatusDTO.idResponse update(UserStatusDTO.update updateDTO);

    UserStatusDTO.idResponse updateByUserId(UUID userId, UserStatusDTO.update updateDTO);

    Long delete(Long id);

    Long delete(UUID uuid);
}
