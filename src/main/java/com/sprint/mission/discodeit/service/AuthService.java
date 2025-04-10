package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.login.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;

public interface AuthService {

  UserDto login(LoginRequest loginRequest);
}
