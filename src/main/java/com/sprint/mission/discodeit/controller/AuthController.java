package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserMapper userMapper;
  private final UserRepository userRepository;

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

    // 세션 무효화
    HttpSession session = httpRequest.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    SecurityContextHolder.clearContext();

    return ResponseEntity.ok(userMapper.toDto(user));
  }

  @GetMapping("/csrf-token")
  public CsrfToken csrfToken(CsrfToken token) {
    return token;
  }
}
