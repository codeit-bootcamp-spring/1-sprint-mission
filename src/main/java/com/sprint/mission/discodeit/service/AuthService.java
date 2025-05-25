package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;

public interface AuthService {
  UserDto updateRole(RoleUpdateRequest roleUpdateRequest);
}
