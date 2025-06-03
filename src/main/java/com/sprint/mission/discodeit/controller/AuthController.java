package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidRefreshTokenException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
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
  private final UserMapper userMapper;

  @GetMapping("csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
    log.debug("CSRF 토큰 요청");
    return ResponseEntity.status(HttpStatus.OK).body(csrfToken);
  }

  @GetMapping("me")
  public ResponseEntity<String> me(@CookieValue("refreshToken") String refreshToken) {
    log.info("AccessToken 조회 요청");

    if (!jwtService.isValidRefreshToken(refreshToken)) {
      throw InvalidRefreshTokenException.expiredOrInvalid();
    }

    User user = jwtService.getSessionByRefresh(refreshToken)
        .map(JwtSession::getUser)
        .orElseThrow(InvalidRefreshTokenException::expiredOrInvalid);

    UserDto userDto = userMapper.toDto(user);

    String accessToken = jwtService.generateAccessToken(userDto);

    return ResponseEntity.ok(accessToken);
  }


  @PostMapping("logout")
  public ResponseEntity<Void> logout(@CookieValue("refreshToken") String refreshToken,
      HttpServletResponse response,
      HttpServletRequest request) {
    log.info("로그아웃 요청: refreshToken = {}", refreshToken);

    try {
      request.logout();
    } catch (ServletException e) {
      log.warn("Spring logout 실패", e);
    }

    if (jwtService.isValidRefreshToken(refreshToken)) {
      jwtService.revokeRefreshToken(refreshToken);
    }

    jwtService.removeTokenCookies(response);

    return ResponseEntity.ok().build();
  }


  @PostMapping("refresh")
  public ResponseEntity<String> refresh(@CookieValue("refreshToken") String refreshToken,
      HttpServletResponse response) {
    log.info("액세스 토큰 재발급 요청");

    try {
      String newAccessToken = jwtService.refreshAccessToken(refreshToken);

      return ResponseEntity.ok(newAccessToken);

    } catch (InvalidRefreshTokenException e) {
      log.warn("유효하지 않은 refresh token 요청");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }



  @PutMapping("role")
  public ResponseEntity<UserDto> role(@RequestBody RoleUpdateRequest request) {
    log.info("권한 수정 요청");

    UserDto userDto = authService.updateRole(request);

    jwtService.forceLogoutByUserId(userDto.id());

    return ResponseEntity.ok(userDto);
  }
}
