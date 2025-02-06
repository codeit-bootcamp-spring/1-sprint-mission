package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.dto.authService.LoginRequest;
import com.sprint.mission.discodeit.dto.userService.UserDTO;

public interface AuthService {
    UserDTO login(LoginRequest loginRequest);
}
