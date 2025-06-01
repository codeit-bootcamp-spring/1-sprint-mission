package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import jakarta.servlet.http.HttpSessionEvent;
//import com.sprint.mission.discodeit.dto.request.LoginRequest;

public interface AuthService {


  UserDto getCurrentUser();

  UserDto initAdmin();

  UserDto updateRole(RoleUpdateRequest request);

//  UserDto login(LoginRequest loginRequest);
}
