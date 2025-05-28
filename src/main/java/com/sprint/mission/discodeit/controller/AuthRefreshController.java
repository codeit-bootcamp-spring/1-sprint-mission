package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.TokenPair;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRefreshController {

  private final JwtService jwtService;

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken,
      HttpServletResponse response) {
    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 없음");
    }

    try {
      TokenPair tokenPair = jwtService.reissueTokens(refreshToken);

      Cookie cookie = new Cookie("refreshToken", tokenPair.refreshToken());
      cookie.setHttpOnly(false);
      cookie.setSecure(true);
      cookie.setPath("/");
      cookie.setMaxAge(60 * 60 * 24 * 21);
      cookie.setAttribute("SameSite", "None");
      response.addCookie(cookie);

      return ResponseEntity.ok(tokenPair.accessToken());

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 없음");
    }
  }
}