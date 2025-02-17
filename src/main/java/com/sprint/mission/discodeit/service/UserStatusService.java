package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.CurrentTimeDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserIdDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusIdDTO;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserStatusService {

    UserStatus created(UserIdDTO data);

    UserStatus find(UUID id);

    List<UserStatus> findAll();

    void update(UserStatusIdDTO idData, CurrentTimeDTO timeData);

    void updateByUserId(UserIdDTO idData, CurrentTimeDTO timeData);

    void delete(UUID id);
}

