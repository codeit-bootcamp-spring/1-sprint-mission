package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;

public interface AuthService {

  UserResponseDto login(LoginRequest loginDTO);
}
