package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.AuthApi;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final UserMapper userMapper;
  private final UserService userService;


  @GetMapping("/csrf-token")
  public CsrfToken csrfToken(CsrfToken token) {
    // Spring Security가 자동으로 CsrfToken 객체를 주입해 줌
    return token;
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> me(Authentication authentication) {
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    return ResponseEntity.ok(userMapper.toDto(userDetails.getUser()));
  }

  @PutMapping("/role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDto> updateUserRole(
          @RequestBody UserRoleUpdateRequest request,
          HttpServletRequest httpRequest,
          Authentication authentication
  ) {

    //현재 로그인 한 사용자 id;
    UUID currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();

    UserDto updated = userService.updateRole(request.getUserId(), request.getNewRole());
    return ResponseEntity.ok(updated);
  }

}
