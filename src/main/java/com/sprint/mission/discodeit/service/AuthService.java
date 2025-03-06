package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.FindUserResponseDto;

public interface AuthService {

    FindUserResponseDto login(LoginRequestDto loginRequestDto);
}
