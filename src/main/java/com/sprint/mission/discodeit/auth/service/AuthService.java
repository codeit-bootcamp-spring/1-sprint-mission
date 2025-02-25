package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.dto.LoginUserRequest;
import com.sprint.mission.discodeit.user.entity.User;

public interface AuthService {
	User login(LoginUserRequest request);
}
