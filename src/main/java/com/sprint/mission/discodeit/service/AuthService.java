package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.AuthServiceLoginDTO;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {
    User login(AuthServiceLoginDTO dto);
}
