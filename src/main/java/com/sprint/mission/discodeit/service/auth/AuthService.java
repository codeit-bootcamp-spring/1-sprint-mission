package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.entity.user.UserLoginRequest;
import com.sprint.mission.discodeit.entity.user.UserLoginResponse;

public interface AuthService {
    UserLoginResponse login(UserLoginRequest loginInfo);
}
