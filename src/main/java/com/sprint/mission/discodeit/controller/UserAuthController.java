package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.role.RoleUpdateRequest;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.auth.UserAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Transactional
@Tag(name = "User Auth Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserAuthController {

  private final UserAuthService userAuthService;
  private final JwtSessionRepository jwtSessionRepository;
  private final JwtService jwtService;

  @GetMapping("/csrf-token")
  public CsrfToken publishCsrfToken(CsrfToken csrfToken) {
    log.info("csrf 토큰 발급: {}", csrfToken);
    return csrfToken;
  }

  /**
   * @methodName : sessionMe
   * @date : 2025. 5. 27. 18:44
   * @author : wongil
   * @Description: 토큰 유지
   **/
  @GetMapping("/me")
  public ResponseEntity<String> tokenMe(@CookieValue(name = "refresh_token") String refreshToken) {

    return userAuthService.tokenMe(refreshToken);
  }

  /**
   * @methodName : refreshToken
   * @date : 2025. 5. 27. 22:01
   * @author : wongil
   * @Description: refresh 토큰으로 accessToken 갱신
   **/
  @PostMapping("/refresh")
  public ResponseEntity<String> refreshToken(
      @CookieValue(name = "refresh_token") String refreshToken, HttpServletResponse response) {

    if (refreshToken == null || !jwtSessionRepository.existsByRefreshToken(refreshToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    JwtSession jwtSession = jwtService.reIssue(refreshToken);

    Cookie cookie = new Cookie("refresh_token", jwtSession.getRefreshToken());
    cookie.setHttpOnly(false);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60 * 24 * 15);
    response.addCookie(cookie);
    log.info("새로운 access token 발급 완료: {}", jwtSession.getAccessToken());

    return ResponseEntity.ok(jwtSession.getAccessToken());
  }

  /**
   * @methodName : logout
   * @date : 2025. 5. 27. 21:48
   * @author : wongil
   * @Description: 로그아웃
   **/
  @PostMapping("/logout")
  public void logout(HttpServletRequest request, HttpServletResponse response) {

    userAuthService.logout(request, response);
    log.info("로그아웃 성공");
  }

  /**
   * @methodName : updateRole
   * @date : 2025. 5. 28. 09:22
   * @author : wongil
   * @Description: 사용자 권한 수정
   **/
  @PreAuthorize("hasRole('ADMIN')")
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/role")
  public void updateRole(@Valid @RequestBody RoleUpdateRequest roleUpdateRequest,
      HttpServletRequest request, HttpServletResponse response) {
    
    userAuthService.updateRole(roleUpdateRequest, request);
    log.info("User Role 변경 완료: {} to {}", roleUpdateRequest.userId(), roleUpdateRequest.newRole());
  }
}
