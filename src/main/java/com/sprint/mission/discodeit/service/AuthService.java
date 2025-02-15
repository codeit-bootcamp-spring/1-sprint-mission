package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;

public interface AuthService {
    UserResponse login(UserRequest.Login request);
}
