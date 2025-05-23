package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;

public interface AuthService {

  UserDto updateUserRole(UserRoleUpdateRequest request);
}
