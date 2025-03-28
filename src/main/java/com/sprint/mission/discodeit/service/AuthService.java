package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;

public interface AuthService {

  // 로그인
  UserDto login(LoginRequest request);
}
