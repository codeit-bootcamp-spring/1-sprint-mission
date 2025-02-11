package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusDTO;

public interface UserStatusService {
    UserStatusDTO create(UserStatusDTO userStatusDTO);
    UserStatusDTO find(String userId);
    void delete(String userId);
}