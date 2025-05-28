package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.AuthApiDocs;
import com.sprint.mission.discodeit.dto.user.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.auth.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.basic.UserOnlineStatusService;
import com.sprint.mission.discodeit.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApiDocs {

  private final AuthService authService;
  private final UserMapper mapper;
  private final UserService userService;
  private final PersistentTokenRepository tokenRepository;
  private final UserOnlineStatusService statusService;

  @GetMapping("/me")
  public ResponseEntity<UserResponseDto> me(Authentication authentication) {
    if (authentication == null
        || !(authentication.getPrincipal() instanceof DiscodeitUserDetails discodeitUser)) {
      return ResponseEntity.status(401).build();
    }

    User user = discodeitUser.getUser();
    return ResponseEntity.ok(mapper.toDto(user, statusService));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response,
      Authentication auth) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    SecurityContextHolder.clearContext();

    Cookie cookie = new Cookie("remember-me", null);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);

    if (auth != null) {
      tokenRepository.removeUserTokens(auth.getName());
    }

    return ResponseEntity.ok().build();
  }

  @PutMapping("/role")
  public ResponseEntity<UserResponseDto> updateUserRole(
      @RequestBody RoleUpdateRequest updateRequest) {

    UserResponseDto responseDto = mapper.toDto(userService.updateUserRole(updateRequest),
        statusService);
    authService.forceLogout(updateRequest.userId());

    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/check-session")
  public ResponseEntity<Void> checkSession() {
    authService.printSessions();
    return ResponseEntity.ok().build();
  }
}
