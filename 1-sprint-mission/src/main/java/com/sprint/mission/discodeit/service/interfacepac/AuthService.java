package com.sprint.mission.discodeit.service.interfacepac;

import com.sprint.mission.discodeit.dto.request.user.UserLoginRequestDTO;
import com.sprint.mission.discodeit.dto.response.user.UserLoginResponseDTO;

public interface AuthService {
    UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO);
}
