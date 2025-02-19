package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.login.LoginRequest;
import com.sprint.mission.discodeit.dto.login.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
