package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.request.LoginUserRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;

public interface AuthService {
	UserResponse login(LoginUserRequest request);
}
