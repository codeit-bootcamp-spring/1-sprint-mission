package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.LoginDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {
    User login(LoginDto loginDto);
}
