package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.exception.auth.UnauthorizedException;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BasicAuthService implements AuthService {

  @Override
  public Authentication getCurrentAuthentication() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
      throw new UnauthorizedException();
    }
    return auth;
  }

  @Override
  public String getCurrentUsername() {
    return getCurrentAuthentication().getName();
  }

  @Override
  public boolean hasRole(Role role) {
    Authentication auth = getCurrentAuthentication();
    return auth.getAuthorities().stream()
        .anyMatch(authority -> authority.getAuthority().equals(role.name()));
  }

  @Override
  public void requireRole(Role requiredRole) {
    if (!hasRole(requiredRole)) {
      throw UnauthorizedException.withRole(requiredRole);
    }
  }

}