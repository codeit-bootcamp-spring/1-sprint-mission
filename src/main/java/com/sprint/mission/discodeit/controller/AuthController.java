package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.AuthControllerDocs;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;
  private final JwtService jwtService;

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> csrfToken(CsrfToken token) {
    return ResponseEntity.ok(token);
  }

  @GetMapping("/me")
  public ResponseEntity<String> getUser(@CookieValue(JwtService.REFRESH_TOKEN_COOKIE_NAME) String refreshToken) {
    JwtSession jwtSession = jwtService.getJwtSession(refreshToken);

    return ResponseEntity.ok(jwtSession.getAccessToken());
  }

  @PutMapping("/role")
  public ResponseEntity<UserDto> updateUserRole(@RequestBody RoleUpdateRequest roleUpdateRequest, HttpServletRequest request, HttpServletResponse response) {
    UserDto userDto = authService.updateUserRole(roleUpdateRequest);
    return ResponseEntity.ok(userDto);
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refresh(
      @CookieValue(JwtService.REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
      HttpServletResponse response
  ) {
    JwtSession jwtSession = jwtService.refreshJwtToken(refreshToken);
    Cookie refreshTokenCookie = new Cookie(JwtService.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    response.addCookie(refreshTokenCookie);

    return ResponseEntity.ok(jwtSession.getAccessToken());
  }
}
