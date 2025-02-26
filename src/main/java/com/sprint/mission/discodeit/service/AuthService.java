package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.LoginRequest;

public interface AuthService {

  UserDto login(LoginRequest loginRequest);
}
