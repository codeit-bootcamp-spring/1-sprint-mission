package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.AuthApiDocs;
import com.sprint.mission.discodeit.dto.user.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.AuthException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.security.auth.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.basic.UserOnlineStatusService;
import com.sprint.mission.discodeit.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApiDocs {

  private final AuthService authService;
  private final UserMapper mapper;
  private final UserService userService;
  private final PersistentTokenRepository tokenRepository;
  private final UserOnlineStatusService statusService;
  private final JwtService jwtService;
  private final JwtSessionRepository jwtSessionRepository;

  @Value("${app.jwt.refresh-token-expiration}")
  private long refreshTokenExpiration;

  @GetMapping("/me")
  public ResponseEntity<String> me(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    if (authentication == null
        || !(authentication.getPrincipal() instanceof DiscodeitUserDetails discodeitUser)) {
      return ResponseEntity.status(401).build();
    }
    String refreshToken = extractRefreshTokenFromCookie(request);

    JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new AuthException(ErrorCode.INVALID_TOKEN));

    User user = discodeitUser.getUser();
    return ResponseEntity.ok(session.getAccessToken());
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response,
      Authentication auth) {

//    HttpSession session = request.getSession(false);
//
//    if (session != null) {
//      session.invalidate();
//    }
//
//    SecurityContextHolder.clearContext();
//
//    Cookie cookie = new Cookie("remember-me", null);
//    cookie.setPath("/");
//    cookie.setMaxAge(0);
//    response.addCookie(cookie);
//
//    if (auth != null) {
//      tokenRepository.removeUserTokens(auth.getName());
//    }
//
//    return ResponseEntity.ok().build();

    String refreshToken = extractRefreshTokenFromCookie(request);

    if (refreshToken != null) {
      jwtService.invalidateRefreshToken(refreshToken);
    }

    Cookie cookie = new Cookie("refresh_token", null);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    cookie.setHttpOnly(true);
    response.addCookie(cookie);

    SecurityContextHolder.clearContext();

    return ResponseEntity.ok().build();
  }


  @PutMapping("/role")
  public ResponseEntity<UserDto> updateUserRole(
      @RequestBody RoleUpdateRequest updateRequest) {

    UserDto responseDto = mapper.toDto(userService.updateUserRole(updateRequest),
        statusService);

    authService.forceLogout(updateRequest.userId());

    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/check-session")
  public ResponseEntity<Void> checkSession() {
    authService.printSessions();
    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refreshToken(HttpServletRequest request,
      HttpServletResponse response) {
    String refreshToken = extractRefreshTokenFromCookie(request);

    if (refreshToken == null || !jwtService.validateToken(refreshToken)) {
      throw new AuthException(ErrorCode.INVALID_TOKEN);
    }

    JwtSession newSession = jwtService.rotateRefreshToken(refreshToken);

    Cookie newCookie = new Cookie("refresh_token", newSession.getRefreshToken());
    newCookie.setHttpOnly(true);
    newCookie.setPath("/");
    newCookie.setMaxAge((int) refreshTokenExpiration);
    response.addCookie(newCookie);

    return ResponseEntity.ok(newSession.getAccessToken());
  }

  private String extractRefreshTokenFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) {
      return null;
    }

    for (Cookie cookie : request.getCookies()) {
      if ("refresh_token".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }
}
