package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.LoginRequestDTO;
import com.sprint.mission.discodeit.DTO.UserDTO;

public interface AuthService {
    boolean login(String name, String password);
}
