package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.AuthDTO;
import com.sprint.mission.discodeit.dto.UserDTO;

public interface AuthService {
    UserDTO.response login(AuthDTO.loginReq loginDTO);
}
