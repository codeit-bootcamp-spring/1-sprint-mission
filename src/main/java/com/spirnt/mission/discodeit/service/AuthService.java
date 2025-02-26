package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.auth.LoginRequest;
import com.spirnt.mission.discodeit.enity.User;

public interface AuthService {
    User login(LoginRequest loginRequest);
}
