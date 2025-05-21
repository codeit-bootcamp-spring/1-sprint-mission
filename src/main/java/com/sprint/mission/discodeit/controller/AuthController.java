package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.user.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  @GetMapping(path = "/csrf-token")
  public CsrfToken getCsrfToken(CsrfToken csrfToken) {
    return csrfToken; // Spring Security가 자동 주입해주는 CsrfToken 객체 반환
  }

  @GetMapping(path = "/me")
  public ResponseEntity<?> getCurrentUser(HttpSession session) {

    UserDto loginUser = (UserDto) session.getAttribute("LOGIN_USER");

    if (loginUser == null) {
      return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
          .body(Map.of("message", "로그인 정보가 없습니다."));
    }

    return ResponseEntity.ok(loginUser);
  }

  @PostMapping(path = "/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

    // 세션 무효화
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    // securityContext 초기화
    SecurityContextHolder.clearContext();

    return ResponseEntity.ok().build();
  }
}
