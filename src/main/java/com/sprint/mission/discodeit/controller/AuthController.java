package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserPrincipal;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final UserMapper userMapper;

  // 1. CSRF 토큰 발급 API
  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> csrf(CsrfToken csrfToken) {
    return ResponseEntity.ok(csrfToken);
  }

  // 2. 현재 사용자 정보 조회 API
  @GetMapping("/me")
  public ResponseEntity<UserDto> me() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
    User user = principal.getUser();
    UserDto userDto = userMapper.toDto(user);
    return ResponseEntity.ok(userDto);
  }

  // 3. 로그아웃 API
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request) {
    // 세션 무효화 및 SecurityContext 초기화
    if (request.getSession(false) != null) {
      request.getSession(false).invalidate();
    }
    SecurityContextHolder.clearContext();
    return ResponseEntity.ok().build();
  }

  @PutMapping("/api/auth/role")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<UserDto> updateUserRole(@RequestBody UserRoleUpdateRequest request, HttpServletRequest httpRequest) {
    UserDto updatedUser = authService.updateUserRole(request);
    // 강제 로그아웃
    httpRequest.getSession().invalidate();
    return ResponseEntity.ok(updatedUser);
  }
}

