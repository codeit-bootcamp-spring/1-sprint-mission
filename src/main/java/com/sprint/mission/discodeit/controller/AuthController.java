package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
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

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
    log.debug("CSRF 토큰 요청");
    return ResponseEntity.status(HttpStatus.OK).body(csrfToken);
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> me(@AuthenticationPrincipal DiscodeitUserDetails userDetails) {
    log.info("내 정보 조회 요청");
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDetails.getUserDto());
  }

  @PutMapping("/role")
  public ResponseEntity<UserDto> role(@RequestBody RoleUpdateRequest request) {
    log.info("권한 수정 요청");
    UserDto userDto = authService.updateRole(request);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDto);
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody UserDto userDto, HttpServletResponse response) {
    String accessToken = jwtService.generateTokens(userDto);
    String refreshToken = jwtService.getAccessTokenByRefresh(userDto.id().toString());
    ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(60 * 60 * 24 * 7)
        .build();
    response.setHeader("Set-Cookie", cookie.toString());
    return ResponseEntity.ok(accessToken);
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refresh(HttpServletRequest request)
      throws ServletException, IOException {
    String refreshToken = request.getCookies()[0].getValue();

    UUID userId = UUID.fromString(request.getHeader("userId"));
    String username = request.getHeader("username");
    String email = request.getHeader("email");
    Part profile = request.getPart("profile");
    boolean online = Boolean.parseBoolean(request.getHeader("online"));
    Role role = Role.valueOf(request.getHeader("role"));

    BinaryContentDto profileDto = new BinaryContentDto(
        null,
        profile.getName(),
        profile.getSize(),
        profile.getContentType()
    );

    UserDto userDto = new UserDto(
        userId,
        username,
        email,
        profileDto,
        online,
        role
    );

    String newAccessToken = jwtService.rotateRefreshToken(refreshToken, userDto);
    return ResponseEntity.ok(newAccessToken);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = request.getCookies()[0].getValue();
    jwtService.invalidate(refreshToken);
    ResponseCookie expiredCookie = ResponseCookie.from("refresh_token", "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .build();
    response.setHeader("Set-Cookie", expiredCookie.toString());
    return ResponseEntity.ok().build();
  }
}
