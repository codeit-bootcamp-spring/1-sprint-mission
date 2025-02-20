package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.login.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;

public interface AuthLoginService {

  UserDto login(AuthLoginRequest request);
}
