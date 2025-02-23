package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.authDto.LoginRequestDto;
import com.sprint.mission.discodeit.dto.userDto.FindUserResponseDto;

public interface AuthService {

    FindUserResponseDto login(LoginRequestDto loginRequestDto);
}
