package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.UserLoginDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증/인가 API")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public UserDto login(@RequestBody UserLoginDto userLoginDto) {
    return authService.login(userLoginDto);
  }
}
