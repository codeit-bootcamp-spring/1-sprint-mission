package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.security.RoleUpdateRequest;

public interface AuthService {

  UserDto updateRole(RoleUpdateRequest request);
}
