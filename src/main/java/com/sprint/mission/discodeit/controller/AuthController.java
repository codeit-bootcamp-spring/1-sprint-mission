package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final JwtService jwtService;

  @GetMapping("csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
    log.debug("CSRF 토큰 요청");
    return ResponseEntity.status(HttpStatus.OK).body(csrfToken);
  }

  @GetMapping("me")
  public ResponseEntity<String> me(
      @CookieValue(value = JwtService.REFRESH_TOKEN_COOKIE_NAME) String refreshToken) {
    log.info("내 정보 조회 요청");
    JwtSession jwtSession = jwtService.getJwtSession(refreshToken);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(jwtSession.getAccessToken());
  }

  @PutMapping("role")
  public ResponseEntity<UserDto> role(@RequestBody RoleUpdateRequest request) {
    log.info("권한 수정 요청");
    UserDto userDto = authService.updateRole(request);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDto);
  }

  @PostMapping("refresh")
  public ResponseEntity<String> refresh(
      @CookieValue(JwtService.REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
      HttpServletResponse response
  ) {
    log.info("토큰 재발급 요청");
    JwtSession jwtSession = jwtService.refreshJwtSession(refreshToken);

    Cookie refreshTokenCookie = new Cookie(JwtService.REFRESH_TOKEN_COOKIE_NAME,
        jwtSession.getRefreshToken());
    refreshTokenCookie.setHttpOnly(true);
    response.addCookie(refreshTokenCookie);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(jwtSession.getAccessToken())
        ;
  }
}
