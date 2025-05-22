package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthMeController {

  private final UserMapper userMapper;

  @GetMapping("/login")
  public ResponseEntity<UserDto> login(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(userMapper.toDto(user));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    // 세션 무효화
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    // SecurityContext 무효화
    SecurityContextHolder.clearContext();

    return ResponseEntity.ok().build();
  }

  @GetMapping("/csrf-token")
  public CsrfToken csrfToken(CsrfToken token) {
    return token;
  }
}
