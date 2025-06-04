package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.AuthApi;
import com.sprint.mission.discodeit.dto.ErrorResponse;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final UserMapper userMapper;
  private final UserService userService;
  private final JwtService jwtService;


  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> csrfToken(CsrfToken csrfToken) {
    return ResponseEntity.status(HttpStatus.OK).body(csrfToken);
  }

  @GetMapping("/me")
  public ResponseEntity<String> me(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new AccessDeniedException("리프레시 토큰 없음");
    }

    String accessToken = jwtService.getAccessTokenByRefreshToken(refreshToken);
    return ResponseEntity.ok(accessToken);
  }

  @PutMapping("/role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDto> updateUserRole(
          @RequestBody UserRoleUpdateRequest request,
          HttpServletRequest httpRequest,
          Authentication authentication
  ) {

    //현재 로그인 한 사용자 id;
    //UUID currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();

    UserDto updated = userService.updateRole(request.getUserId(), request.getNewRole());
    return ResponseEntity.ok(updated);
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(
          @CookieValue(value = "refresh_token", required = false) String refreshToken) {

    if (refreshToken == null || refreshToken.isBlank()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(ErrorResponse.of(
                      ErrorCode.MISSING_REFRESH_TOKEN,
                      Map.of("cookie", "refresh_token not found"),
                      this.getClass()
              ));
    }

    try {
      String newAccessToken = jwtService.refreshToken(refreshToken);

      ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
              .path("/")
              .httpOnly(true)
              .maxAge(60 * 60 * 24 * 21)
              .build();

      return ResponseEntity.ok()
              .header(HttpHeaders.SET_COOKIE, cookie.toString())
              .body(newAccessToken);

    } catch (IllegalStateException | IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(ErrorResponse.of(
                      ErrorCode.INVALID_REFRESH_TOKEN,
                      Map.of("reason", e.getMessage()),
                      e.getClass()
              ));
    }
  }


}
