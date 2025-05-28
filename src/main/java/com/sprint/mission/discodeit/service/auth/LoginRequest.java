package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.entity.role.RoleUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginRequest {

  void updateRole(RoleUpdateRequest request, HttpServletRequest httpRequest);
}
