package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignInDto;

public interface AuthService {
    UserResponseDto login(UserSignInDto loginDTO);
}
