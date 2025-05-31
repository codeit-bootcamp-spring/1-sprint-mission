package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.UserDetailsAdapter;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtService.JwtTokens;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final UserService userService;
  private final JwtService jwtService;


  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      Optional<Cookie> refreshTokenCookie = Arrays.stream(cookies)
          .filter(cookie -> "refresh_token".equals(cookie.getName()))
          .findFirst();

      refreshTokenCookie.ifPresent(cookie -> {
        jwtService.invalidateToken(cookie.getValue());

        Cookie clearCookie = new Cookie("refresh_token", "");
        clearCookie.setMaxAge(0);
        clearCookie.setPath("/");
        response.addCookie(clearCookie);
      });
    }

    // SecurityContext 무효화
    SecurityContextHolder.clearContext();

    return ResponseEntity.ok().build();
  }

  @GetMapping("/me")
  public ResponseEntity<String> me(HttpServletRequest request) {
    log.info("내 정보 조회 요청");

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      Optional<Cookie> refreshTokenCookie = Arrays.stream(cookies)
          .filter(cookie -> "refresh_token".equals(cookie.getName()))
          .findFirst();

      if (refreshTokenCookie.isPresent()) {
        String refreshToken = refreshTokenCookie.get().getValue();
        Optional<String> accessTokenOpt = jwtService.getAccessTokenByRefreshToken(refreshToken);

        if (accessTokenOpt.isPresent()) {
          return ResponseEntity.ok("\"" + accessTokenOpt.get() + "\"");
        }
      }
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refreshToken(HttpServletRequest request,
      HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      Optional<Cookie> refreshTokenCookie = Arrays.stream(cookies)
          .filter(cookie -> "refresh_token".equals(cookie.getName()))
          .findFirst();

      if (refreshTokenCookie.isPresent()) {
        String refreshToken = refreshTokenCookie.get().getValue();
        Optional<JwtTokens> tokensOpt = jwtService.refreshToken(refreshToken);

        if (tokensOpt.isPresent()) {
          JwtTokens tokens = tokensOpt.get();

          Cookie newRefreshTokenCookie = new Cookie("refresh_token", tokens.refreshToken());
          newRefreshTokenCookie.setHttpOnly(false);
          newRefreshTokenCookie.setSecure(request.isSecure());
          newRefreshTokenCookie.setPath("/");
          newRefreshTokenCookie.setMaxAge(604800);
          response.addCookie(newRefreshTokenCookie);

          return ResponseEntity.ok("\"" + tokens.accessToken() + "\"");
        }
      }
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @PutMapping("/role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDto> updateRole(
      @RequestBody RoleUpdateRequest request, HttpServletRequest httpRequest
  ) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> UserNotFoundException.withId(request.userId()));
    user.getRoles().clear();
    user.getRoles().add(request.newRole());
    userRepository.save(user);

    jwtService.invalidateAllUserSessions(user.getId());

    // SecurityContext 무효화
    SecurityContextHolder.clearContext();

    return ResponseEntity.ok(userMapper.toDto(user));
  }

  @GetMapping("/csrf-token")
  public CsrfToken csrfToken(CsrfToken token) {
    return token;
  }
}
