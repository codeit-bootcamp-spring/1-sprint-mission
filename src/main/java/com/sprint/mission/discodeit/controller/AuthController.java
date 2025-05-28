package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증/인가 API")
public class AuthController {

  private final UserService userService;
  private final JwtService jwtService;

  @GetMapping("/csrf-token")
  public CsrfToken getCsrfToken(CsrfToken csrfToken) {
    return csrfToken;
  }

  @GetMapping("/me")
  public ResponseEntity<String> getCurrentUser(Authentication authentication,
      HttpServletRequest request) {

    if (authentication == null || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    try {
      String refreshToken = extractRefreshTokenFromCookie(request);
      String accessToken = jwtService.reissueAccessTokens(refreshToken);

      return ResponseEntity.ok().body(accessToken);

    } catch (Exception e) {
      log.error("Failed to get current user", e);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PutMapping("/role")
  public ResponseEntity<UserDto> updateRole(@RequestBody RoleUpdateRequest userRoleUpdateRequest) {

    UserDto userDto = userService.updateUserRole(userRoleUpdateRequest);
    return ResponseEntity.ok(userDto);
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refreshToken(HttpServletRequest request) {
    try {
      // 쿠키에서 리프레시 토큰 추출
      String refreshToken = extractRefreshTokenFromCookie(request);

      if (refreshToken == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }

      // 리프레시 토큰 검증 및 새 엑세스 토큰 생성
      String accessToken = jwtService.reissueAccessTokens(refreshToken);

      return ResponseEntity.ok().body(accessToken);
    } catch (Exception e) {
      log.error("Failed to refresh token", e);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

  }

  private String extractRefreshTokenFromCookie(HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("refresh_token".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

}
