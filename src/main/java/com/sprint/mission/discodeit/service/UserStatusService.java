package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatus create(UserStatusCreateDTO userStatusCreateDTO);

    UserStatus find(UUID uuid);

    List<UserStatus> findAll();

    UserStatus update(UserStatusUpdateDTO userStatusUpdateDTO);

    UserStatus updateByUserId(UserStatusUpdateDTO userStatusUpdateDTO);

    void delete(UUID uuid);

}
