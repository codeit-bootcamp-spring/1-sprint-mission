package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.entity.auth.RequestLogin;
import com.sprint.mission.discodeit.entity.auth.ResponseLogin;

public interface AuthService {

  ResponseLogin login(RequestLogin request);
}
