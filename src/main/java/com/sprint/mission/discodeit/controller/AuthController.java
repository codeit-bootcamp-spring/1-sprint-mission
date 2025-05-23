package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.CustomUserDetails;
import com.sprint.mission.discodeit.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.auth.UserLoginDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증/인가 API")
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  @GetMapping("/csrf-token")
  public CsrfToken getCsrfToken(CsrfToken csrfToken) {
    return csrfToken;
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {

    if (authentication == null || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    try {
      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      UserDto userDto = userService.findByUsername(userDetails.getUsername());

      return ResponseEntity.ok(userDto);

    } catch (Exception e) {
      log.error("Failed to get current user", e);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PutMapping("/role")
  public ResponseEntity<UserDto> updateRole(@RequestBody RoleUpdateRequest userRoleUpdateRequest) {

    UserDto userDto = userService.updateUserRole(userRoleUpdateRequest);
    return ResponseEntity.ok(userDto);
  }

}
