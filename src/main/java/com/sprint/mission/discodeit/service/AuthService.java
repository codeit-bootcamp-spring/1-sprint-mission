package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.CustomUserDetails;

public interface AuthService {

//  UserDto login(LoginRequest loginRequest);

    UserDto getUserInfo(CustomUserDetails userDetails);

    UserDto updateUserRole(UserRoleUpdateRequest request);
}
