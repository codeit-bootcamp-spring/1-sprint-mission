package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.JwtSession;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserMapper userMapper;
  private String refreshToken;

//  @PostMapping(path = "login")
//  public ResponseEntity<UserDto> login(@Valid @RequestBody LoginRequest loginRequest) {
//    UserDto user = authService.login(loginRequest);
//    return ResponseEntity
//        .status(HttpStatus.OK)
//        .body(user);
//  }

//  @GetMapping("/me")
//  public UserDto me(Authentication authentication) {
//    return userMapper.toDto((User) authentication.getPrincipal());
//  }
//
//  @PostMapping("/logout")
//  public void logout(HttpServletRequest request, HttpServletResponse response) {
//    request.getSession(false).invalidate();
//    SecurityContextHolder.clearContext();
//    response.setStatus(HttpServletResponse.SC_OK);
//  }

  @GetMapping("/me")
  public ResponseEntity<String> getAccessTokenFromRefreshToken(
      @CookieValue(name = "refreshToken", required = false) String refreshToken) {
    this.refreshToken = refreshToken;
    if (refreshToken == null || jwtService.isRefreshTokenValid(refreshToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    Optional<JwtSession> sessionOpt = jwtService.findJwtSessionByRefreshToken(refreshToken);
    if (sessionOpt.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String accessToken = sessionOpt.get().getAccessToken();
    return ResponseEntity.ok(accessToken);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @CookieValue(name = "refreshToken", required = false) String refreshToken,
      HttpServletResponse response) {
    if (refreshToken != null) {
      jwtService.invalidateRefreshToken(refreshToken);
      ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
          .httpOnly(true)
          .path("/")
          .maxAge(0)
          .build();
      response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refreshAccessToken(
      @CookieValue(name = "refreshToken", required = false) String refreshToken,
      HttpServletResponse response) {
    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<String> newAccessTokenOpt = jwtService.reissueAccessTokenWithRotation(refreshToken,
        response);

    return newAccessTokenOpt.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

  }


  @PutMapping("/role")
  @PreAuthorize("hasRole('ADMIN')")
  public UserDto updateRole(@RequestBody UserRoleUpdateRequest request) {
    jwtService.invalidateSessionsByUserId(userService.findByUserName(request.username()));
    return userService.updateRoles(request);
  }

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final BasicUserService userService;

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginRequest request,
      HttpServletResponse response) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password())
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetails user = (UserDetails) authentication.getPrincipal();
    UserDto userDto = userService.convertToUserDto(user);

    String accessToken = jwtService.generateAccessToken(userDto);
    String refreshToken = jwtService.generateRefreshToken(userDto);

    jwtService.saveJwtSession(userDto, accessToken, refreshToken);

    ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
        .httpOnly(true)
        .path("/")
        .maxAge(Duration.ofDays(7))
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(accessToken);
  }
}
