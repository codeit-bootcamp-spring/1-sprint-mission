package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.dto.authDto.LoginRequest;

public interface AuthService {

  UserDto login(LoginRequest loginRequest);
}
