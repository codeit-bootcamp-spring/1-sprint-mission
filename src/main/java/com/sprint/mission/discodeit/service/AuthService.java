package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.UserRoleUpdateRequest;

public interface AuthService {

    UserDto updateUserRole(UserRoleUpdateRequest request);
}
