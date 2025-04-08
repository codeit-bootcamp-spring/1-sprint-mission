package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.UserLoginDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@Valid @RequestBody UserLoginDto userLoginDto) {
    try {
      UserDto loginUser = authService.login(userLoginDto);
      return ResponseEntity.ok(loginUser);
    } catch (UserException e) {
      log.error(e.getMessage());
      throw e;
    }
  }
}
