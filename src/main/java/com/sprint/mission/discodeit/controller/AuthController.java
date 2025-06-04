package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.auth.TokenPair;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<String> getCurrentUser(HttpServletRequest request,
      HttpServletResponse response) {

    String refreshToken = extractRefreshTokenFromCookie(request);
    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 토큰 재발급
    TokenPair tokens = jwtService.reissueAccessTokens(refreshToken);

    // 새 리프레시 토큰으로 쿠키 업데이트
    Cookie refreshTokenCookie = new Cookie("refresh_token", tokens.refreshToken());
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(false); // 운영 환경에서는 true
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30); // 30일
    response.addCookie(refreshTokenCookie);

    // 액세스 토큰 반환
    return ResponseEntity.ok(tokens.accessToken());

  }

  @PutMapping("/role")
  public ResponseEntity<UserDto> updateRole(@RequestBody RoleUpdateRequest userRoleUpdateRequest) {

    UserDto userDto = userService.updateUserRole(userRoleUpdateRequest);
    return ResponseEntity.ok(userDto);
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refreshToken(HttpServletRequest request,
      HttpServletResponse response) {

    // 쿠키에서 리프레시 토큰 추출
    String refreshToken = extractRefreshTokenFromCookie(request);

    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 토큰 재발급
    TokenPair tokens = jwtService.reissueAccessTokens(refreshToken);

    // 새 리프레시 토큰으로 쿠키 업데이트
    Cookie refreshTokenCookie = new Cookie("refresh_token", tokens.refreshToken());
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(false); // 운영 환경에서는 true
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30); // 30일
    response.addCookie(refreshTokenCookie);

    // 액세스 토큰 반환
    return ResponseEntity.ok(tokens.accessToken());

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
