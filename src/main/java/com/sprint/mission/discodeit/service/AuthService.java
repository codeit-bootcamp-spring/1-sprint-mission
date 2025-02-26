package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.LoginResponseDto;

public interface AuthService {
  LoginResponseDto login(String username, String password);
}
