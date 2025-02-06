package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserLoginRequestDTO;
import com.sprint.mission.discodeit.dto.response.UserLoginResponseDTO;

public interface AuthService {
    UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO);
}
