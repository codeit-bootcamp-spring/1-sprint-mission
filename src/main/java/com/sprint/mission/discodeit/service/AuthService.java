package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.LoginDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;

public interface AuthService {
    UserResponseDto login(LoginDto loginDto);
}
