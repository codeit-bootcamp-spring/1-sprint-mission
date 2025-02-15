package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.LoginDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;

public interface AuthService {
    UserResponseDto login(LoginDto loginDto);
}
