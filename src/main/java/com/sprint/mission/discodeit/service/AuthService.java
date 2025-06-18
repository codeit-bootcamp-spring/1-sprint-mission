package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;

public interface AuthService {

  UserDto initAdmin();

  UserDto updateRole(RoleUpdateRequest request);
}
