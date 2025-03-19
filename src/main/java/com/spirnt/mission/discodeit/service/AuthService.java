package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.auth.LoginRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;

public interface AuthService {

  UserDto login(LoginRequest loginRequest);
}
