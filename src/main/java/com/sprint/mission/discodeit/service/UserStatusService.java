package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatusType;

public interface UserStatusService {
    UserStatusDto create(UserStatusDto userStatusDTO);
    UserStatusDto find(String userId);
    void delete(String userId);
    UserStatusType getUserOnlineStatus(String userId);
}