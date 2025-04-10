package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.auth.AuthUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;

public interface AuthService {

  UserDto isUserExist(AuthUserRequest authUserRequest);

}
