package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.user.AuthRequestDTO;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {
    User login(AuthRequestDTO request);
}
