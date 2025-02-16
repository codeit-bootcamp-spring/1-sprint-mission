package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.LoginUserRequest;
import com.sprint.mission.discodeit.dto.user.LoginUserResponse;

public interface AuthService {
    LoginUserResponse login(LoginUserRequest request);
}
