package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Role;
import org.springframework.security.core.Authentication;

public interface AuthService {

  Authentication getCurrentAuthentication();

  String getCurrentUsername();

  boolean hasRole(Role role);

  void requireRole(Role requiredRole);

}
