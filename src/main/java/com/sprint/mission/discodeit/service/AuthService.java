package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;

public interface AuthService {
  UserResponseDto login(String username, String password);
}
