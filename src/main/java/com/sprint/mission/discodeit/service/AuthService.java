package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.AuthRequest;
import com.sprint.mission.discodeit.dto.AuthResponse;

import java.util.Optional;

public interface AuthService {
    Optional<AuthResponse> login(AuthRequest authRequest);
}
