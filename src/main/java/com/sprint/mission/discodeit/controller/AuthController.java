package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.security.JwtService;
import com.sprint.mission.discodeit.security.JwtSession;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final JwtService jwtService;
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletResponse response) {
    UserDto userDto = authService.authenticate(request.username(), request.password());

    var session = jwtService.generateTokens(userDto);

    Cookie refreshCookie = new Cookie("refresh_token", session.getRefreshToken());
    refreshCookie.setHttpOnly(true);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge((int) (60 * 60 * 24 * 14)); // 14일
    response.addCookie(refreshCookie);

    return ResponseEntity.ok(session.getAccessToken());
  }
  @GetMapping("/me")
  public ResponseEntity<String> me(HttpServletRequest request) {
    String refreshToken = null;

    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("refresh_token".equals(cookie.getName())) {
          refreshToken = cookie.getValue();
          break;
        }
      }
    }

    if (refreshToken == null) {
      return ResponseEntity.status(401).body("Missing refresh token");
    }

    try {
      String accessToken = jwtService.findByRefreshToken(refreshToken).getAccessToken();
      return ResponseEntity.ok(accessToken);
    } catch (RuntimeException e) {
      return ResponseEntity.status(401).body("Invalid refresh token");
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@CookieValue("refresh_token") String refreshToken,
      HttpServletResponse response) {
    jwtService.invalidateRefreshToken(refreshToken);

    // 쿠키 무효화
    ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
        .httpOnly(true)
        .path("/")
        .maxAge(0)
        .build();

    response.addHeader("Set-Cookie", deleteCookie.toString());

    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refresh(@CookieValue("refresh_token") String refreshToken,
      HttpServletResponse response) {
    try {
      JwtSession session = jwtService.rotateRefreshToken(refreshToken);

      // 새 refresh 쿠키 설정
      ResponseCookie newCookie = ResponseCookie.from("refresh_token", session.getRefreshToken())
          .httpOnly(true)
          .path("/")
          .maxAge(60 * 60 * 24 * 14) // 2주
          .build();

      response.addHeader("Set-Cookie", newCookie.toString());

      return ResponseEntity.ok(session.getAccessToken());
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

//  @GetMapping("csrf-token")
//  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
//    log.debug("CSRF 토큰 요청");
//    return ResponseEntity.status(HttpStatus.OK).body(csrfToken);
//  }
//
//  @GetMapping("me")
//  public ResponseEntity<UserDto> me(@AuthenticationPrincipal DiscodeitUserDetails userDetails) {
//    log.info("내 정보 조회 요청");
//    return ResponseEntity
//        .status(HttpStatus.OK)
//        .body(userDetails.getUserDto());
//  }
//
//  @PutMapping("role")
//  public ResponseEntity<UserDto> role(@RequestBody RoleUpdateRequest request) {
//    log.info("권한 수정 요청");
//    UserDto userDto = authService.updateRole(request);
//
//    return ResponseEntity
//        .status(HttpStatus.OK)
//        .body(userDto);
//  }
}
