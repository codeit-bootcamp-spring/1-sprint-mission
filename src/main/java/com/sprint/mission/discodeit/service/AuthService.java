package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.FindUserDto;

public interface AuthService {
  FindUserDto login(String userName, String password);
}
