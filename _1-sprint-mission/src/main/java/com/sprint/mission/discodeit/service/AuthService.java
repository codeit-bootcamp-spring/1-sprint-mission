package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.login.LoginDto;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {
    User login(LoginDto loginDto);
}
