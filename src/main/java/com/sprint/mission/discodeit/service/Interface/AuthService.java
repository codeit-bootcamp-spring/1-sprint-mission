package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.user.AuthRequestDto;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {
    User login(AuthRequestDto request);
}
