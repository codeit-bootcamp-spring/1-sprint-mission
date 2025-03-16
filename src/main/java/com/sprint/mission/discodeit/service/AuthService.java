package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;

public interface AuthService {

    UserDto login(LoginRequestDto loginRequestDto);
}
