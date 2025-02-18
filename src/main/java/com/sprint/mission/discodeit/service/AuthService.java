package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.request.LoginRequest;
import com.sprint.mission.discodeit.dto.user.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
