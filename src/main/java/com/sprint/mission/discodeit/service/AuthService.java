package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;

public interface AuthService {

    UserDto login(LoginRequest loginRequest);
}
