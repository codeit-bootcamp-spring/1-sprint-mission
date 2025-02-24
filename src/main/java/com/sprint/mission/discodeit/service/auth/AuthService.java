package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.dto.authService.LoginRequest;
import com.sprint.mission.discodeit.dto.authService.LoginResponse;
import com.sprint.mission.discodeit.dto.userService.UserDTO;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {
    User login(LoginRequest loginRequest);}
