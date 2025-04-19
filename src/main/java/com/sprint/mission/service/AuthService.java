package com.sprint.mission.service;

import com.sprint.mission.dto.request.LoginRequest;
import com.sprint.mission.entity.User;

public interface AuthService {

  User login(LoginRequest loginRequest);
}
