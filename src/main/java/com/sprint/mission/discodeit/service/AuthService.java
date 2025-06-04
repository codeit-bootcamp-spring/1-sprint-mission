package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

  String login(LoginRequest loginRequest, HttpServletResponse response);

  void logout(String refreshToken, HttpServletResponse response);

  String refresh(String refreshToken, HttpServletResponse response);
}
