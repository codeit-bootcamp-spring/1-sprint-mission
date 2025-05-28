package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthStatusController {
  private final UserService userService;
  private final JwtService jwtService;

  @GetMapping("/me")
  public ResponseEntity<String> getAccessToken(@CookieValue("refreshToken") String refreshToken) {
    return jwtService.findAccessTokenByRefreshToken(refreshToken)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token"));
  }


  @PostMapping("/auth/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    SecurityContextHolder.clearContext();
    return ResponseEntity.ok().build();
  }

  @PutMapping("/auth/role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDto> updateRole(@RequestBody RoleUpdateRequest request) {
    UserDto userDto=userService.updateUserRole(request.getUserId(), request.getNewRole());
    jwtService.invalidateAllSessionsByUserId(request.getUserId());

    return ResponseEntity.ok(userDto);
  }

}