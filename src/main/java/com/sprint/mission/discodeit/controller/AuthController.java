package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import com.sprint.mission.discodeit.security.jwt.JwtTokenDto;
import com.sprint.mission.discodeit.service.basic.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthenticationManager authenticationManager;
  //
  private final AuthService authService;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final JwtSessionRepository jwtSessionRepository;

  @GetMapping(value = "/csrf-token")
  public ResponseEntity<CsrfToken> crsf(CsrfToken token) {
    return ResponseEntity.ok(token);
  }

  @GetMapping(value = "/me")
  public ResponseEntity<UserDto> me() {
    return ResponseEntity.ok(authService.getUserBySession());
  }

  @PutMapping(value = "/role")
  public ResponseEntity<UserDto> updateRole(@RequestBody RoleUpdateRequest request) {
    return ResponseEntity.ok(authService.changeRole(request));
  }

  @PostMapping(value = "/login")
  public ResponseEntity<String> login(
      @RequestBody LoginRequest loginRequest,
      HttpServletResponse response
  ) {
    try {
      // 사용자 인증
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
      );

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();

      User user = userRepository.findByUsername(userDetails.getUsername())
          .orElseThrow(
              () -> new UserNotFoundException(Map.of("username", loginRequest.username())));

      UserDto userDto = userMapper.toDto(user);
      JwtTokenDto jwtTokenDto = jwtService.generateTokensAndSaveSession(userDto);
      String refreshToken = jwtTokenDto.refreshToken();
      String accessToken = jwtTokenDto.accessToken();

      // Refresh Token 쿠키에 저장
      int maxAge = (int) ChronoUnit.SECONDS.between(LocalDateTime.now(),
          // jwtService 에서 가져오거나 yml에서 값가져오는 걸로 변경할 수 있을듯
          jwtSessionRepository.findByRefreshToken(refreshToken).get().getRefreshTokenExpiresAt());
      ResponseCookie cookie = ResponseCookie.from("refreshToken",
              refreshToken) // 이거 그대로... 저장해도 되는걸까요
          .httpOnly(true)
          .secure(true)
          .sameSite("Strict")
          .path("/api/auth/") // 해당 경로에만 정
          .maxAge(maxAge)
          .build();

      response.addHeader("Set-Cookie", cookie.toString());
      return ResponseEntity.ok()
          .contentType(MediaType.TEXT_PLAIN)
          .body(accessToken);
    } catch (BadCredentialsException e) {
      log.warn("로그인에 실패했습니다. | 잘못된 자격 증명: {}", loginRequest.username());
      return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
          .contentType(MediaType.TEXT_PLAIN)
          .body("인증 처리 중 오류가 발생했습니다: " + e.getMessage());
    }
  }
}
