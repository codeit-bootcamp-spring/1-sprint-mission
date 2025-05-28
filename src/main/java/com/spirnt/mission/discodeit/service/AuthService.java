package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.security.CustomUserDetails;

public interface AuthService {

    UserDto getMe(CustomUserDetails customUserDetails);

    UserDto updateRole(UserRoleUpdateRequest userRoleUpdateRequest);
}
