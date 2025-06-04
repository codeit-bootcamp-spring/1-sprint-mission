package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;

public interface AuthService {

    String getMe(String refreshToken);

    UserDto updateRole(UserRoleUpdateRequest userRoleUpdateRequest);
}
